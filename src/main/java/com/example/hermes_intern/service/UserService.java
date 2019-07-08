package com.example.hermes_intern.service;

import com.example.hermes_intern.security.JWTUtil;
import com.example.hermes_intern.security.PBKDF2Encoder;
import com.example.hermes_intern.security.model.*;
import com.example.hermes_intern.repository.ReactiveUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserService {

    // this is just an example, you can load the user from the database from the repository

    private final ReactiveUserRepository reactiveUserRepository;

    private PBKDF2Encoder passwordEncoder;

    private JWTUtil jwtUtil;

    @Autowired
    public UserService(ReactiveUserRepository reactiveUserRepository, PBKDF2Encoder passwordEncoder, JWTUtil jwtUtil) {
        this.reactiveUserRepository = reactiveUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return this.reactiveUserRepository.findByUsername(ar.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    public Mono<Message> create(RegisterRequest registerRequest) {

        return this.reactiveUserRepository.findByUsername(registerRequest.getUsername())
                .map(user -> new Message("Username Is Already In Use"))
                .switchIfEmpty(Mono.just(new Message("User Created")).map(x ->
                {
                    List<Role> roles = new ArrayList<>();

                    try {
                        for (String role : registerRequest.getRoles()) {
                            roles.add(Role.valueOf(role));
                        }
                    } catch (IllegalArgumentException e) {
                        x.setMessage(e.getMessage());
                        return x;
                    }

                    User user = new User();
                    user.setUsername(registerRequest.getUsername());
                    user.setId(UUID.randomUUID().toString());
                    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                    user.setRoles(roles);
                    this.reactiveUserRepository.save(user).subscribe();

                    return x;
                }));

    }

    @View
    public Flux<User> getAll() {
        return this.reactiveUserRepository.findAll();
    }

}
