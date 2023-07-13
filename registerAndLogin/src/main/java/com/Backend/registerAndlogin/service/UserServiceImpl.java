package com.Backend.registerAndlogin.service;
import com.Backend.registerAndlogin.entity.User;
import com.Backend.registerAndlogin.entity.UserToken;
import com.Backend.registerAndlogin.repository.UserRepo;
import com.Backend.registerAndlogin.repository.UserTokenRepo;
import com.Backend.registerAndlogin.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserTokenRepo userTokenRepo;

    @Override
    public boolean checkEmail(User user){
        return userRepo.existsByEmail(user.getEmail());
    }

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public String generateToken(String username) {
        UserToken userToken=new UserToken();
        UserDetails userDetails = loadUserByUsername(username);
        String token=jwtTokenProvider.generateToken(userDetails);
        userToken.setToken(token);
        userTokenRepo.save(userToken);
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUserName(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public User getUserByUsername(String userName) {
        Optional<User> user = Optional.ofNullable(userRepo.findByUserName(userName));
        return user.orElse(null);
    }

    @Override
    public Authentication authenticateUser(String userName, String password) {
        return null;
    }


}
