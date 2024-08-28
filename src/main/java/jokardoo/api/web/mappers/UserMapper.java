package jokardoo.api.web.mappers;

import jokardoo.api.domain.user.User;
import jokardoo.api.web.dto.user.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto dto);

    UserDto toDto(User user);

    List<UserDto> toDto(List<User> users);
}
