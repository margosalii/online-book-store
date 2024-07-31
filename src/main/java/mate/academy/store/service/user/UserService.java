package mate.academy.store.service.user;

import mate.academy.store.dto.user.UserRegistrationRequestDto;
import mate.academy.store.dto.user.UserResponseDto;
import mate.academy.store.exception.RegistrationException;

public interface UserService {
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
