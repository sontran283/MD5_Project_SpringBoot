package com.ra.controller;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserResponseAllDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.service.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;

//    @GetMapping("/users")
//    public ResponseEntity<Page<UserResponseDTO>> index(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int limit) throws CustomException {
//        Pageable pageable = PageRequest.of(page, limit);
//        Page<UserResponseDTO> responses = userService.findAll(pageable);
//        return new ResponseEntity<>(responses, HttpStatus.OK);
//    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponseAllDTO>> getListUser(Pageable pageable) throws CustomException {
        Page<UserResponseAllDTO> userResponseDTOList = userService.findAll(pageable);
        return new ResponseEntity<>(userResponseDTOList, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> create_user(@RequestBody @Valid UserRequestDTO userRequestDTO) throws CustomException {
        UserResponseDTO userResponseDTO = userService.saveOrUpdate(userRequestDTO);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete_user(@PathVariable("id") Long id) throws CustomException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> edit_user(@PathVariable("id") Long id) throws CustomException {
        UserResponseDTO userResponseDTO = userService.findById(id);
        if (userResponseDTO != null) {
            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

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

    @GetMapping("/users/search-sort-pagination")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "order", defaultValue = "asc") String order,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "page", defaultValue = "0") int page) throws CustomException {
        if (!order.equals("asc") && !order.equals("desc")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (search != null && search.isEmpty()) {
            search = null;
        }
        Pageable pageable = order.equals("asc") ?
                PageRequest.of(page, size, Sort.by(sort).ascending()) :
                PageRequest.of(page, size, Sort.by(sort).descending());
        Page<UserResponseDTO> userPage = userService.searchByName(pageable, search);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }


    // cập nhật trạng thái user
    @PatchMapping("/users/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws CustomException {
        try {
            userService.changeStatus(id);
            UserResponseDTO userResponseDTO = userService.findById(id);

            // Check if the user is an ADMIN
            if (userResponseDTO.getRoles().contains("ADMIN")) {
                return new ResponseEntity<>("Cannot change status for ADMIN", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    // phân quyền
    @PatchMapping("/users/{id}/change-role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id) throws CustomException {
        userService.changeUserRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
