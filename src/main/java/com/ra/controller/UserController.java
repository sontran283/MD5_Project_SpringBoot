package com.ra.controller;

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
    public ResponseEntity<List<UserResponseAllDTO>> getListUser() throws CustomException {
        List<UserResponseAllDTO> userResponseDTOList = userService.findAll();
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

    @GetMapping("/users/search+sort+pagination")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(@RequestParam(name = "search") String search, @RequestParam(name = "sort", defaultValue = "id") String sort, @RequestParam(name = "order", defaultValue = "asc") String order, @RequestParam(name = "size", defaultValue = "5") int size, @RequestParam(name = "page", defaultValue = "0") int page) throws CustomException {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<UserResponseDTO> userPage = userService.searchByName(pageable, search);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }

    // cập nhật trạng thái user
    @PatchMapping("/users/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) throws CustomException {
        userService.changeStatus(id);
        UserResponseDTO userResponseDTO = userService.findById(id);
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    // phân quyền
    @PatchMapping("/users/{id}/change-role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id) throws CustomException {
        userService.changeUserRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
