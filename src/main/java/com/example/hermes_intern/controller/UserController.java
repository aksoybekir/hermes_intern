package com.example.hermes_intern.controller;

import com.example.hermes_intern.security.model.User;
import com.example.hermes_intern.security.model.AuthRequest;
import com.example.hermes_intern.security.model.AuthResponse;
import com.example.hermes_intern.security.JWTUtil;
import com.example.hermes_intern.security.PBKDF2Encoder;
import com.example.hermes_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService ) {
        this.userService = userService;
    }

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private UserService userRepository;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return this.userService.login(ar);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Mono<User> create() {
        return this.userService.create();
    }

}