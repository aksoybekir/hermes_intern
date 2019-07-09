package com.example.hermes_intern.unit;

import com.example.hermes_intern.repository.ReactiveUserRepository;
import com.example.hermes_intern.security.JWTUtil;
import com.example.hermes_intern.security.PBKDF2Encoder;
import com.example.hermes_intern.security.model.*;
import com.example.hermes_intern.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Mock
    private ReactiveUserRepository reactiveUserRepository;

    @Mock
    private PBKDF2Encoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    private UserService userService;

    @Before
    public void setup() {

        userService = new UserService(reactiveUserRepository, passwordEncoder, jwtUtil);

    }

    @Test
    public void usersShouldLoginTest() {

        User ahmetUser = new User();
        ahmetUser.setUsername("ahmet");
        ahmetUser.setPassword("mM2TDtzbhS5/QbUGTrNGqU3wD6isHAjOnZ1WfFeZONs=");

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("ahmet");
        authRequest.setPassword("1");

        when(reactiveUserRepository.findByUsername(authRequest.getUsername())).thenReturn(Mono.just(ahmetUser));
        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn(ahmetUser.getPassword());
        when(jwtUtil.generateToken(ahmetUser)).thenReturn("abcdToken");

        StepVerifier.create(userService.login(authRequest))
                .consumeNextWith(expectedResult -> {
                    assertThat(expectedResult).isNotNull();
                    assertThat(expectedResult.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(expectedResult.getBody()).isNotNull();
                    assertThat(expectedResult.getBody().getToken()).isEqualTo("abcdToken");
                })
                .verifyComplete();
    }

    @Test
    public void wrongCredentialsShouldGetUnauthorizedErrorTest() {

        User ahmetUser = new User();
        ahmetUser.setUsername("ahmet");
        ahmetUser.setPassword("mM2TDtzbhS5/QbUGTrNGqU3wD6isHAjOnZ1WfFeZONs=");

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("ahmet");
        authRequest.setPassword("Wrong password");

        when(reactiveUserRepository.findByUsername(authRequest.getUsername())).thenReturn(Mono.just(ahmetUser));
        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn("Encoded Wrong Password");
        when(jwtUtil.generateToken(ahmetUser)).thenReturn("abcdToken");

        StepVerifier.create(userService.login(authRequest))
                .consumeNextWith(expectedResult -> {
                    assertThat(expectedResult).isNotNull();
                    assertThat(expectedResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    assertThat(expectedResult.getBody()).isNotNull();
                    assertThat(expectedResult.getBody().getToken()).isEqualTo("");
                })
                .verifyComplete();
    }

    @Test
    public void nonRegisteredUserNamesShouldGetUnauthorizedErrorTest() {

        User ahmetUser = new User();
        ahmetUser.setUsername("ahmet");
        ahmetUser.setPassword("mM2TDtzbhS5/QbUGTrNGqU3wD6isHAjOnZ1WfFeZONs=");

        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("Non Registeren User Name");
        authRequest.setPassword("1");

        when(reactiveUserRepository.findByUsername(authRequest.getUsername())).thenReturn(Mono.empty());
        when(passwordEncoder.encode(authRequest.getPassword())).thenReturn(ahmetUser.getPassword());
        when(jwtUtil.generateToken(ahmetUser)).thenReturn("abcdToken");

        StepVerifier.create(userService.login(authRequest))
                .consumeNextWith(expectedResult -> {
                    assertThat(expectedResult).isNotNull();
                    assertThat(expectedResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    assertThat(expectedResult.getBody()).isNotNull();
                    assertThat(expectedResult.getBody().getToken()).isEqualTo("");
                })
                .verifyComplete();
    }

    @Test
    public void shouldSuccessfullyCreateUserIfNotExistsTest()
    {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("ahmet");
        registerRequest.setPassword("1");
        List<String> roles =  new ArrayList<>();
        roles.add("ROLE_CUSTOMER");
        registerRequest.setRoles(roles);

        User ahmetUser = new User();
        ahmetUser.setUsername("ahmet");
        ahmetUser.setPassword("mM2TDtzbhS5/QbUGTrNGqU3wD6isHAjOnZ1WfFeZONs=");

        when(reactiveUserRepository.findByUsername(registerRequest.getUsername())).thenReturn(Mono.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(ahmetUser.getPassword());
        when(reactiveUserRepository.save(any(User.class))).thenReturn(Mono.empty());


        StepVerifier.create(userService.create(registerRequest))
                .consumeNextWith(expectedResult ->{
                    assertThat(expectedResult).isNotNull();
                    assertThat(expectedResult.getMessage()).isEqualTo("User Created");
                })
                .verifyComplete();
    }

    @Test
    public void ShouldReturnErrorMessageIfUserNameExistsTest()
    {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("ahmet");
        registerRequest.setPassword("1");
        List<String> roles =  new ArrayList<>();
        roles.add("ROLE_CUSTOMER");
        registerRequest.setRoles(roles);

        User ahmetUser = new User();
        ahmetUser.setUsername("ahmet");
        ahmetUser.setPassword("mM2TDtzbhS5/QbUGTrNGqU3wD6isHAjOnZ1WfFeZONs=");

        when(reactiveUserRepository.findByUsername(registerRequest.getUsername())).thenReturn(Mono.just(ahmetUser));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(ahmetUser.getPassword());

        StepVerifier.create(userService.create(registerRequest))
                .consumeNextWith(expectedResult ->{
                    assertThat(expectedResult).isNotNull();
                    assertThat(expectedResult.getMessage()).isEqualTo("Username Is Already In Use");
                })
                .verifyComplete();
    }
}
