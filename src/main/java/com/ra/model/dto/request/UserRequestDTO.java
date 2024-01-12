package com.ra.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
    @NotEmpty(message = "Cannot be left blank")
    private String userName;
    @Size(min = 3, max = 100, message = "Cannot be left blank")
    private String password;
    @NotEmpty(message = "Cannot be left blank")
    @Email(message = "Must have @")
    private String email;
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phoneNumber;
    @NotEmpty(message = "Cannot be left blank")
    private String address;
}
