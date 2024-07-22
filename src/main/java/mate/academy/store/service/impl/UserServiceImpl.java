package mate.academy.store.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.UserRegistrationRequestDto;
import mate.academy.store.dto.UserResponseDto;
import mate.academy.store.exception.RegistrationException;
import mate.academy.store.mapper.UserMapper;
import mate.academy.store.model.User;
import mate.academy.store.repository.user.UserRepository;
import mate.academy.store.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register new user with email: "
                + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        User registeredUser = userRepository.save(user);
        return userMapper.toResponseDto(registeredUser);
    }
}
