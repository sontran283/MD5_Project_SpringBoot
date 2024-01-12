package com.ra.service;


import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.ProductResponseDTO;
import com.ra.model.dto.response.UserResponseAllDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User register(User user);

    UserResponseDTO login(UserRequestDTO userRequestDTO);

    Page<UserResponseAllDTO> findAll(Pageable pageable) throws CustomException;

    void delete(Long id) throws CustomException;

    UserResponseDTO saveOrUpdate(UserRequestDTO userRequestDTO) throws CustomException;

    UserResponseDTO findById(Long id) throws CustomException;

    Page<UserResponseAllDTO> searchByName(Pageable pageable, String name) throws CustomException;

    void changeStatus(Long id) throws CustomException;

    void changeUserRole(Long id) throws CustomException;
}