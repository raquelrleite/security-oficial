package br.com.securityoficial.controller;

import br.com.securityoficial.dto.request.LoginRequest;
import br.com.securityoficial.dto.request.UserRequest;
import br.com.securityoficial.dto.response.LoginResponse;
import br.com.securityoficial.dto.response.UserResponse;
import br.com.securityoficial.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid UserRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody String refreshToken) {
        return service.refresh(refreshToken);
    }

    @GetMapping("/{username}")
    public UserResponse findByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String username) {
        service.delete(username);
    }
}
