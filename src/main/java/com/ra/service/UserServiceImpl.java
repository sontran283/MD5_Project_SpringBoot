package com.ra.service;


import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserResponseDTO;
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

        return UserResponseDTO.builder().token(jwtProvider.generateToken(userPrinciple))
                .userName(userPrinciple.getUsername())
                .roles(userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> userPage=userRepository.findAll(pageable);
        return userPage.map(user -> new User(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getStatus(),user.getRoles()));
    }

    @Override
    public Page<User> searchByName(Pageable pageable, String name) {
        Page<User> userPage=userRepository.findAllByUserNameContainingIgnoreCase(pageable, name);
        return userPage.map(user -> new User(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(), user.getStatus(),user.getRoles()));
    }
}