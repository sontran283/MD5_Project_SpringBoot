package com.ra.model.dto.response;

import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserResponseAllDTO {
    private Long id;
    private String userName;
    private Set<String> roles;
    private String email;
    private String phoneNumber;
    private String address;

    public UserResponseAllDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
    }
}
