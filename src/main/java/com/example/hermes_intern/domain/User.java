package com.example.hermes_intern.domain;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.annotation.Id;

public class User {
    @Id
    private String id;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("role")
    private UserRoles role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
