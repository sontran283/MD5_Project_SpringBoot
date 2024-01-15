package com.ra.controller.admin;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserResponseAllDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;

    // sort-pagination
    @GetMapping("/users/sort-pagination")
    public ResponseEntity<?> userIndex(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "3") int limit,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order) throws CustomException {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }
        Page<UserResponseAllDTO> userResponseAllDTOS = userService.findAll(pageable);
        return new ResponseEntity<>(userResponseAllDTOS, HttpStatus.OK);
    }

    // search
    @GetMapping("/users/search")
    public ResponseEntity<Page<UserResponseAllDTO>> userSearch(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "3") int limit,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "search") String search) throws CustomException {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, limit, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, limit, Sort.by(sort).descending());
        }
        Page<UserResponseAllDTO> userResponseAllDTOS = userService.searchByName(pageable, search);
        return new ResponseEntity<>(userResponseAllDTOS, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseAllDTO>> getListUser(Pageable pageable) throws CustomException {
        Page<UserResponseAllDTO> userResponseDTOList = userService.findAll(pageable);
        return new ResponseEntity<>(userResponseDTOList, HttpStatus.OK);
    }

    // add
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> create_user(@RequestBody @Valid UserRequestDTO userRequestDTO) throws CustomException {
        UserResponseDTO userResponseDTO = userService.saveOrUpdate(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    // edit
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> edit_user(@PathVariable("id") Long id) throws CustomException {
        UserResponseDTO userResponseDTO = userService.findById(id);
        if (userResponseDTO != null) {
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // update
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> update_user(@PathVariable("id") Long id, @RequestBody UserRequestDTO userRequestDTO) throws CustomException {
        UserResponseDTO userResponseDTO = userService.findById(id);
        if (userResponseDTO != null) {
            userResponseDTO.setUserName(userRequestDTO.getUserName());
            userResponseDTO.setEmail(userRequestDTO.getEmail());
            UserResponseDTO newUserResponseDTO = userService.saveOrUpdate(userRequestDTO);
            return new ResponseEntity<>(newUserResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // change status
    @PatchMapping("/users/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws CustomException {
        try {
            userService.changeStatus(id);
            UserResponseDTO userResponseDTO = userService.findById(id);

            if (userResponseDTO.getRoles().contains("ADMIN")) {
                return new ResponseEntity<>("Cannot change status for ADMIN", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // change Role
    @PatchMapping("/users/{id}/change-role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id) throws CustomException {
        userService.changeUserRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete_user(@PathVariable("id") Long id) throws CustomException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
