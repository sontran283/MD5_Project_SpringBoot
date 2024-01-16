package com.ra.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class UserRequestDTO {
    private Long id;
    @NotBlank(message = "Cannot be left blank")
    private String userName;
    @Size(min = 3, max = 100, message = "Cannot be left blank")
    @NotBlank(message = "Cannot be left blank")
    private String password;
    @NotBlank(message = "Cannot be left blank")
    @Email(message = "Must have @")
    private String email;
    @Pattern(regexp = "(0[3|5|7|8|9])+([0-9]{8})\\b", message = "Enter the Vietnamese phone")
    private String phoneNumber;
    @NotBlank(message = "Cannot be left blank")
    private String address;
}
