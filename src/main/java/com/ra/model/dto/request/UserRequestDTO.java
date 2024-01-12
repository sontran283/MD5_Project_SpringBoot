package com.ra.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserRequestDTO {
    @NotEmpty(message = "Please fill in userName")
    private String userName;
    @Size(min = 3, message = "Enter 8 numbers")
    private String password;
    @Email(message = "Invalid format email")
    private String email;
    @NotEmpty(message = "Please fill in phoneNumber")
    private String phoneNumber;
    private String address;
}
