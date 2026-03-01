package br.com.securityoficial.service;

import br.com.securityoficial.dto.request.LoginRequest;
import br.com.securityoficial.dto.request.UserRequest;
import br.com.securityoficial.dto.response.LoginResponse;
import br.com.securityoficial.dto.response.UserResponse;
import br.com.securityoficial.entity.User;
import br.com.securityoficial.enums.UserRole;
import br.com.securityoficial.exception.BusinessException;
import br.com.securityoficial.infra.security.TokenService;
import br.com.securityoficial.mapper.UserMapper;
import br.com.securityoficial.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.securityoficial.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse register(UserRequest request) {
        validateUniqueness(request);

        User user = mapper.toEntity(request);
        user.setPassword(encoder.encode(request.password()));

        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }

        repository.save(user);
        return mapper.toResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        String identifier = (request.username() != null) ? request.username() : request.email();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, request.password())
            );
        } catch (AuthenticationException e) {
            throw new BusinessException(INVALID_CREDENTIALS);
        }

        User user = repository.findByUsernameOrEmail(identifier)
                .orElseThrow(() -> new BusinessException(INVALID_CREDENTIALS));

        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.refreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refresh(String refreshToken) {
        Long userId = Long.valueOf(tokenService.validateToken(refreshToken));

        var user = repository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = tokenService.refreshToken(user);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Cacheable(value = "users", key = "#username")
    public UserResponse findByUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(
                        () -> new BusinessException(USER_NOT_FOUND));
        return mapper.toResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public void delete(String username) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        repository.delete(user);
    }

    private void validateUniqueness(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BusinessException(EMAIL_ALREADY_EXISTS);
        }
        if (repository.existsByUsername(request.username())) {
            throw new BusinessException(USERNAME_ALREADY_EXISTS);
        }
    }
}
