package com.ra.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Cannot be left blank")
    private String email;
    @NotEmpty(message = "Cannot be left blank")
    @Column(unique = true)
    private String userName;
    @NotEmpty(message = "Cannot be left blank")
    private String password;
    @NotEmpty(message = "Cannot be left blank")
    private String phoneNumber;
    @NotEmpty(message = "Cannot be left blank")
    private String address;
    @Column(columnDefinition = "Boolean default true")
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
