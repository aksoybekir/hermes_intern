package com.example.hermes_intern.service;

import com.example.hermes_intern.security.PBKDF2Encoder;
import com.example.hermes_intern.security.model.Role;
import com.example.hermes_intern.security.model.User;
import com.example.hermes_intern.repository.ReactiveUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;

@Service
public class UserService {

    // this is just an example, you can load the user from the database from the repository

    private final ReactiveUserRepository users;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    public UserService(ReactiveUserRepository users) {
        this.users= users;
    }


    //username:passwowrd -> user:user
    private final String userUsername = "user";// password: user
    private final User user = new User(userUsername, "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, Arrays.asList(Role.CUSTOMER));

    //username:passwowrd -> admin:admin
    private final String adminUsername = "admin";// password: admin
    private final User admin = new User(adminUsername, "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true, Arrays.asList(Role.ADMIN));

    public Mono<User> findByUsername(String username) {
        if (username.equals(userUsername)) {
            return Mono.just(user);
        } else if (username.equals(adminUsername)) {
            return Mono.just(admin);
        } else {
            return Mono.empty();
        }
    }


    public Mono<User> create() {
        User user = new User("beq");
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode("beq"));
        user.setRoles(Arrays.asList(Role.ADMIN));
        return this.users.save(user);
    }

    @View
    public Flux<User> getAll(){
        return this.users.findAll();
    }

}
