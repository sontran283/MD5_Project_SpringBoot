package com.ra.controller;

import com.ra.model.entity.User;
import com.ra.service.UserService;
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
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getListUser() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> create_user(@RequestBody User user) {
        User user1 = userService.saveOrUpdate(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete_user(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> edit_user(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update_user(@PathVariable("id") Long id, @RequestBody User user) {
        User user1 = userService.findById(id);
        user1.setUserName(user.getUserName());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        User newUser = userService.saveOrUpdate(user1);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @GetMapping("/users/search+sort+pagination")
    public ResponseEntity<Page<User>> getUsers(@RequestParam(name = "search") String search,
                                               @RequestParam(name = "sort", defaultValue = "id") String sort,
                                               @RequestParam(name = "order", defaultValue = "asc") String order,
                                               @RequestParam(name = "size", defaultValue = "5") int size,
                                               @RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<User> userPage = userService.searchByName(pageable, search);
        return new ResponseEntity<>(userPage, HttpStatus.OK);
    }
}
