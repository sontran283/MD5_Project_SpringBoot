package com.ra.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Cannot be left blank")
    private String email;
    @Column(unique = true)
    @NotBlank(message = "Cannot be left blank")
    private String userName;
    @NotBlank(message = "Cannot be left blank")
    private String password;
    @NotBlank(message = "Cannot be left blank")
    private String phoneNumber;
    @NotBlank(message = "Cannot be left blank")
    private String address;
    @Column(columnDefinition = "Boolean default true")
    @NotBlank(message = "Cannot be left blank")
    private Boolean status = true;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private Cart cart;
}
