package com.example.hermes_intern.controller;

import com.example.hermes_intern.domain.User;
import com.example.hermes_intern.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public Mono<User> create(@RequestBody User user) {
        return this.userService.create(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Flux<User> getAll(){ return this.userService.getAll();}

}