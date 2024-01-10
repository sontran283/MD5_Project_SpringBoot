package com.ra.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserRequestDTO {
    private String userName;
    private String password;
}
