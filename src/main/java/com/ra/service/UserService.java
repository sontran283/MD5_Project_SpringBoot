package com.ra.service;


import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User register(User user);

    UserResponseDTO login(UserRequestDTO userRequestDTO);

    List<User> findAll();

    void delete(Long id);

    User saveOrUpdate(User user);

    User findById(Long id);

    Page<User> getAll(Pageable pageable);

    Page<User> searchByName(Pageable pageable, String name);
}