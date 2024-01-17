package com.ra.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequestDTO {
    private Long id;
    @NotEmpty(message = "Cannot be left blank")
    private String userName;
    @Size(min = 3, max = 100, message = "Cannot be left blank")
    @NotEmpty(message = "Cannot be left blank")
    private String password;
    @NotEmpty(message = "Cannot be left blank")
    @Email(message = "Must have @")
    private String email;
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phoneNumber;
    @NotEmpty(message = "Cannot be left blank")
    private String address;
    private Boolean status = true;
}
