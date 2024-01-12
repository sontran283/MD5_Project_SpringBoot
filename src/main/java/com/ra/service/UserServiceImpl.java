package com.ra.service;


import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.*;
import com.ra.model.entity.Product;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.security.jwt.JWTProvider;
import com.ra.security.user_principle.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private RoleService roleService;

    @Override
    public User register(User user) {
        // ma hoa mat khau
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // role
        Set<Role> roles = new HashSet<>();
        // register cua user thi coi no la USER
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            roles.add(roleService.findByRoleName("USER"));
        } else {
            // tao tai khoan va phan quyen thi phai co quyen ADMIN
            user.getRoles().forEach(role -> roles.add(roleService.findByRoleName(role.getName())));
        }
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setAddress(user.getAddress());
        newUser.setStatus(user.getStatus());
        newUser.setRoles(roles);
        return userRepository.save(newUser);
    }

    @Override
    public UserResponseDTO login(UserRequestDTO userRequestDTO) {
        Authentication authentication;
        authentication = authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(userRequestDTO.getUserName(), userRequestDTO.getPassword()));
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return UserResponseDTO
                .builder()
                .token(jwtProvider.generateToken(userPrinciple))
                .userName(userPrinciple.getUsername())
                .roles(userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public List<UserResponseAllDTO> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponseAllDTO::new).toList();
    }

    @Override
    public void delete(Long id) throws CustomException {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO saveOrUpdate(UserRequestDTO userRequestDTO) throws CustomException {
        if (userRepository.existsByUserName(userRequestDTO.getUserName())) {
            throw new CustomException("userName đã tồn tại");
        }
        User user = User.builder()
                .userName(userRequestDTO.getUserName())
                .password(userRequestDTO.getPassword())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .address(userRequestDTO.getAddress())
                .build();
        User userNew = userRepository.save(user);
        return UserResponseDTO.builder()
                .id(userNew.getId())
                .userName(userNew.getUserName())
                .email(userNew.getEmail())
                .phoneNumber(userNew.getPhoneNumber())
                .address(userNew.getAddress())
                .build();
    }

    @Override
    public UserResponseDTO findById(Long id) throws CustomException {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("Not Found"));

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponseDTO
                .builder()
                .id(user.getId())
                .userName(user.getUserName())
                .roles(roleNames)
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .status(user.getStatus())
                .build();
    }

    @Override
    public Page<UserResponseDTO> searchByName(Pageable pageable, String name) {
        Page<User> userPage = userRepository.findAllByUserNameContainingIgnoreCase(pageable, name);
        return userPage.map(UserResponseDTO::new);
    }

    @Override
    public void changeStatus(Long id) throws CustomException {
        UserResponseDTO userResponseDTO = findById(id);
        userRepository.changeStatus(id);
    }

    @Override
    public void changeUserRole(Long id) throws CustomException {
//        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
//
//        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))) {
//            throw new CustomException("User is already an ADMIN and cannot be downgraded to USER.");
//        }
//
//        // Lọc và cập nhật vai trò trong danh sách của người dùng
//        user.getRoles().forEach(role -> {
//            if (role.getName().equals("USER")) {
//                role.setName("ADMIN");
//            }
//        });
//        userRepository.save(user);
        UserResponseDTO userResponseDTO = findById(id);
        if (userResponseDTO.getRoles().stream().anyMatch(role -> role.equals("ADMIN"))) {
            throw new CustomException("User is already an ADMIN and cannot be downgraded to USER.");
        }

        // Lọc và cập nhật vai trò trong danh sách của người dùng


        userResponseDTO.getRoles().removeIf(role -> role.equals("USER"));
        userResponseDTO.getRoles().add("ADMIN");

        Set<Role> roles = userResponseDTO.getRoles().stream().map(role -> roleService.findByRoleName(role)).collect(Collectors.toSet());
        userRepository.save(User.builder()
                .id(userResponseDTO.getId())
                .userName(userResponseDTO.getUserName())
                .address(userResponseDTO.getAddress())
                .roles(roles)
                .phoneNumber(userResponseDTO.getPhoneNumber())
                .status(userResponseDTO.getStatus())
                .email(userResponseDTO.getEmail())
                .build());
    }
}