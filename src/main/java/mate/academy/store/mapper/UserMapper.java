package mate.academy.store.mapper;

import mate.academy.store.config.MapperConfig;
import mate.academy.store.dto.UserRegistrationRequestDto;
import mate.academy.store.dto.UserResponseDto;
import mate.academy.store.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}
