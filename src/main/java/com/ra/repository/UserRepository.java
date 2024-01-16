package com.ra.repository;

import com.ra.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByUserNameContainingIgnoreCase(Pageable pageable, String name);

    User findByUserName(String userName);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByAddress(String address);

    @Modifying
    @Query("update User u set u.status=case when u.status=true then false else true end where u.id=?1")
    void changeStatus(Long id);
}
