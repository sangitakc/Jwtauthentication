package com.Backend.registerAndlogin.controller;

import com.Backend.registerAndlogin.entity.User;
import com.Backend.registerAndlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/registerPage")
    public ModelAndView showRegistrationPage(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration.html");
        model.addAttribute("user", new User());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute User user) {
        ModelAndView modelAndView = new ModelAndView();
        boolean userExists = userService.checkEmail(user);
        if (userExists) {
            modelAndView.addObject("message", "User already exists");
            modelAndView.setViewName("redirect:/api/login");
        } else {
            User registeredUser = userService.registerUser(user);
            if (registeredUser != null) {
                modelAndView.addObject("message", "User registered successfully. Please proceed to login.");
                modelAndView.setViewName("redirect:/api/login");
            } else {
                modelAndView.addObject("message", "Something went wrong");
                modelAndView.setViewName("redirect:/api/login");
            }
        }
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView showLoginPage(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login.html");
        model.addAttribute("user", new User());
        return modelAndView;
    }

    @PostMapping(value = "/loginPage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView loginUser(@ModelAttribute User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
            );
            String token = userService.generateToken(user.getUserName());
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("token", token);
            modelAndView.setViewName("redirect:/api/image");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("error", "Invalid username or password");
            modelAndView.setViewName("redirect:/api/login?error=1");
            return modelAndView;
        }
    }



    @GetMapping("/api/image")
    public ModelAndView displayImage(@RequestParam(required = false) String token, @RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView();

        if (token != null) {
            modelAndView.addObject("token", token);
        } else if (error != null) {
            modelAndView.addObject("error", "Invalid username or password");
        }

        modelAndView.setViewName("image.html");

        return modelAndView;
    }

}






