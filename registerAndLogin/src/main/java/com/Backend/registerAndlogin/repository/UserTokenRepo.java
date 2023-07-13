package com.Backend.registerAndlogin.repository;

import com.Backend.registerAndlogin.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepo extends JpaRepository<UserToken, Integer> {

boolean existsByToken(String token);
}
