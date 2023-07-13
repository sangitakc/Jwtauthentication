package com.Backend.registerAndlogin.repository;

import com.Backend.registerAndlogin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public boolean existsByEmail(String email);
    User findByUserName(String userName);
}
