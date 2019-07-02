package com.example.hermes_intern.service;

import com.example.hermes_intern.domain.User;
import com.example.hermes_intern.domain.UserRoles;
import com.example.hermes_intern.repository.ReactiveDeliveryRepository;
import com.example.hermes_intern.repository.ReactiveUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.query.View;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {

    private final ReactiveUserRepository users;


    @Autowired
    public UserService(ReactiveUserRepository users) {
        this.users = users;
    }

    public Mono<User> create(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword("pass");
        user.setUser_role(UserRoles.ADMIN);
        user.setUsername("user");
        return this.users.save(user);
    }

    @View
    public Flux<User> getAll(){
        return this.users.findAll();
    };

    public Mono<User> getByUsernameAndPassword(String username, String password)
    {
        return this.users.getByUsernameAndAndPassword(username,password);
    }

}
