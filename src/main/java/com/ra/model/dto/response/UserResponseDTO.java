package com.ra.model.dto.response;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserResponseDTO {
    private String token;
    private String userName;
    private Set<String> roles;
}
