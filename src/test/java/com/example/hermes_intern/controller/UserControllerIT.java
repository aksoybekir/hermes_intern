package com.example.hermes_intern.controller;

import com.example.hermes_intern.controller.UserController;
import com.example.hermes_intern.repository.ReactiveUserRepository;
import com.example.hermes_intern.security.model.AuthRequest;
import com.example.hermes_intern.security.model.RegisterRequest;
import com.example.hermes_intern.security.model.Role;
import com.example.hermes_intern.security.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "storage.bucket=HermesTest")
@AutoConfigureWebTestClient
public class UserControllerIT {

    @Autowired
    private ReactiveUserRepository reactiveUserRepository;

    @Autowired
    UserController userController;

    @Autowired
    WebTestClient webTestClient;

    User ahmetUser;

    @Before
    public void setUp() {

        ahmetUser = new User();
        ahmetUser.setUsername("Ahmet");
        ahmetUser.setPassword("oZtOruiQ7T9S/FZa0SS9Y8nK4vdnY6m5Vkk1Dg1DioY=");
        ahmetUser.setId(UUID.randomUUID().toString());

        List<Role> roles = new ArrayList<>();
        roles.add(Role.ROLE_ADMIN);

        ahmetUser.setRoles(roles);

        reactiveUserRepository.save(ahmetUser).subscribe();
    }

    @After
    public void tearDown() {
        reactiveUserRepository.deleteAll().subscribe();
    }

    @Test
    public void loginWithCorrectCredentialsShouldLogUserIn() {

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("Ahmet");
        authRequest.setPassword("1");

        webTestClient.post()
                .uri("/users/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.token").isNotEmpty();

    }

    @Test
    public void loginWithIncorrectCredentialsShouldReturnEmptyToken() {

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("Ahmet");
        authRequest.setPassword("wrong Password");

        webTestClient.post()
                .uri("/users/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.token").isEmpty();


    }

    @Test
    public void loginWithNonRegisteredCredentialsShouldReturnEmptyToken() {

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("Non Registered Username");
        authRequest.setPassword("Password");

        webTestClient.post()
                .uri("/users/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.token").isEmpty();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void shoudCreateUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("Mehmet");
        registerRequest.setPassword("1");

        List<String> roles = new ArrayList<>();
        roles.add(Role.ROLE_COURIER.toString());

        registerRequest.setRoles(roles);

        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(registerRequest), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.message").isEqualTo("User Created");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerWithUsedCredentialsShoudReturnErrorMessage() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("Ahmet");
        registerRequest.setPassword("1");

        List<String> roles = new ArrayList<>();
        roles.add(Role.ROLE_COURIER.toString());

        registerRequest.setRoles(roles);

        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(registerRequest), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Username Is Already In Use");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void registerWithEmptyPasswordShoudReturnErrorMessage() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("Veli");
        registerRequest.setPassword("");

        List<String> roles = new ArrayList<>();
        roles.add(Role.ROLE_COURIER.toString());

        registerRequest.setRoles(roles);

        webTestClient.post()
                .uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(registerRequest), RegisterRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Password Cannot Be Empty");
    }

}
