package br.com.securityoficial.mapper;

import br.com.securityoficial.dto.request.UserRequest;
import br.com.securityoficial.dto.response.UserResponse;
import br.com.securityoficial.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);
}

