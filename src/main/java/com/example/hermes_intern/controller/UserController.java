package com.example.hermes_intern.controller;

import com.example.hermes_intern.security.model.*;
import com.example.hermes_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return this.userService.login(ar);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Message> create(@RequestBody RegisterRequest registerRequest) {
        return this.userService.create(registerRequest);
    }

}