package com.Backend.registerAndlogin.service;

import com.Backend.registerAndlogin.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserService extends UserDetailsService{
    User registerUser(User user);
    boolean checkEmail(User user);
    String generateToken(String username);
    String extractTokenFromRequest(HttpServletRequest request);
    User getUserByUsername(String userName);

    Authentication authenticateUser(String userName, String password);
}
