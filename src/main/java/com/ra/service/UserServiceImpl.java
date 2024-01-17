package com.ra.service;


import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.*;
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
    public User register(User user) throws CustomException {
        // kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new CustomException("Username already exists");
        }

        // kiểm tra xem email đã tồn tại chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("Email already exists");
        }

        // kiểm tra xem số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new CustomException("PhoneNumber already exists");
        }

        // mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // role
        Set<Role> roles = new HashSet<>();
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            roles.add(roleService.findByRoleName("USER"));
        } else {
            user.getRoles().forEach(role -> roles.add(roleService.findByRoleName(role.getName())));
        }

        // Tạo đối tượng người dùng mới
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setAddress(user.getAddress());
        newUser.setStatus(user.getStatus());
        newUser.setRoles(roles);
        newUser.setCart(user.getCart());

        // Lưu người dùng mới
        return userRepository.save(newUser);
    }

    @Override
    public UserResponseDTO login(UserRequestDTO userRequestDTO) {
        Authentication authentication;
        authentication = authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(userRequestDTO.getUserName(), userRequestDTO.getPassword()));
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        System.out.println(userPrinciple.getUsername());
        return UserResponseDTO.builder()
                .id(userPrinciple.getUserId())
                .token(jwtProvider.generateToken(userPrinciple))
                .userName(userPrinciple.getUsername())
                .roles(userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .email(userPrinciple.getUser().getEmail())
                .phoneNumber(userPrinciple.getUser().getPhoneNumber())
                .address(userPrinciple.getUser().getAddress())
                .status(userPrinciple.getUser().getStatus())
                .build();
    }


    @Override
    public Page<UserResponseAllDTO> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(UserResponseAllDTO::new);
    }

    @Override
    public void delete(Long id) throws CustomException {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO saveOrUpdate(UserRequestDTO userRequestDTO) throws CustomException {
        if (userRepository.existsByUserName(userRequestDTO.getUserName())) {
            throw new CustomException("username already exists!");
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
    public UserResponseAllDTO findByIdd(Long id) throws CustomException {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("Not Found"));

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponseAllDTO
                .builder()
                .id(user.getId())
                .userName(user.getUserName())
                .roles(roleNames)
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

    @Override
    public Page<UserResponseAllDTO> searchByName(Pageable pageable, String name) {
        Page<User> userPage = userRepository.findAllByUserNameContainingIgnoreCase(pageable, name);
        return userPage.map(UserResponseAllDTO::new);
    }

    @Override
    public void changeStatus(Long id) throws CustomException {
        UserResponseDTO userResponseDTO = findById(id);
        userRepository.changeStatus(id);
    }

    @Override
    public void changeUserRole(Long id) throws CustomException {
        UserResponseDTO userResponseDTO = findById(id);
        User user = userRepository.findById(id).orElse(null);
        if (userResponseDTO.getRoles().stream().anyMatch(role -> role.equals("ADMIN"))) {
            throw new CustomException("Already an ADMIN and cannot be downgraded to USER");
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
                .password(user.getPassword())
                .build());
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }
        return (User) authentication.getPrincipal();
    }

    @Override
    public boolean validateOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void changePassword(User user, String newPassword) throws CustomException {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserResponseAllDTO changeProfile(Long id, UserRequestDTO userRequestDTO) throws CustomException {
        User user = userRepository.findById(id).orElse(null);
//        if (userRepository.existsByUserName(userRequestDTO.getUserName())) {
//            throw new CustomException("UserName already exists!");
//        }
        assert user != null;
        user.setUserName(userRequestDTO.getUserName());
        user.setAddress(userRequestDTO.getAddress());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        userRepository.save(user);
        return new UserResponseAllDTO(user);
    }
}