package br.com.securityoficial.service;

import br.com.securityoficial.dto.request.LoginRequest;
import br.com.securityoficial.dto.request.UserRequest;
import br.com.securityoficial.dto.response.UserResponse;
import br.com.securityoficial.entity.User;
import br.com.securityoficial.enums.ErrorCode;
import br.com.securityoficial.enums.UserRole;
import br.com.securityoficial.exception.BusinessException;
import br.com.securityoficial.infra.security.TokenService;
import br.com.securityoficial.mapper.UserMapper;
import br.com.securityoficial.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.securityoficial.enums.ErrorCode.*;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder, TokenService tokenService) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

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

    public String login(LoginRequest request) {

        User user = repository.findByUsernameOrEmail(request.username(), request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        return tokenService.generateToken(user);
    }

    public List<UserResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private void validateUniqueness(UserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BusinessException(EMAIL_ALREADY_EXISTS);
        }
        if (repository.existsByUsername(request.username())) {
            throw new BusinessException(USERNAME_ALREADY_EXISTS);
        }
    }

    public UserResponse findByUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(
                        () -> new BusinessException(USER_NOT_FOUND));
        return mapper.toResponse(user);
    }

    public void delete(String email) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        repository.delete(user);
    }
}
