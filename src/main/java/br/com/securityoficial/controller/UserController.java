package br.com.securityoficial.controller;

import br.com.securityoficial.dto.request.LoginRequest;
import br.com.securityoficial.dto.request.UserRequest;
import br.com.securityoficial.dto.response.UserResponse;
import br.com.securityoficial.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest request) {
         return service.login(request);
    }

    @GetMapping("{username}")
    public UserResponse findByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return service.findAll();
    }

    @DeleteMapping("{email}")
    public void delete(@PathVariable String email) {
        service.delete(email);
    }
}
