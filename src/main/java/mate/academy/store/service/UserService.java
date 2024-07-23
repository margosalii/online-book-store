package mate.academy.store.service;

import mate.academy.store.dto.UserRegistrationRequestDto;
import mate.academy.store.dto.UserResponseDto;
import mate.academy.store.exception.RegistrationException;

public interface UserService {
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
