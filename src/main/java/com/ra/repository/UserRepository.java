package com.ra.repository;

import com.ra.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByUserNameContainingIgnoreCase(Pageable pageable, String name);
    User findByUserName(String userName);
}
