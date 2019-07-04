package com.example.hermes_intern.service;

import com.example.hermes_intern.security.JWTUtil;
import com.example.hermes_intern.security.PBKDF2Encoder;
import com.example.hermes_intern.security.model.AuthRequest;
import com.example.hermes_intern.security.model.AuthResponse;
import com.example.hermes_intern.security.model.Role;
import com.example.hermes_intern.security.model.User;
import com.example.hermes_intern.repository.ReactiveUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private JWTUtil jwtUtil;


    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return this.users.findByUsername(ar.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
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
