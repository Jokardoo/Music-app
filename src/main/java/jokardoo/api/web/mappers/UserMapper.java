package jokardoo.api.web.mappers;

import jokardoo.api.domain.user.User;
import jokardoo.api.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
//    User toEntity(UserDto dto);
//
//    UserDto toDto(User user);
//
//    List<UserDto> toDto(List<User> users);
}
