package mate.academy.store.service.user.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.user.UserRegistrationRequestDto;
import mate.academy.store.dto.user.UserResponseDto;
import mate.academy.store.exception.RegistrationException;
import mate.academy.store.mapper.UserMapper;
import mate.academy.store.model.RoleName;
import mate.academy.store.model.User;
import mate.academy.store.repository.role.RoleRepository;
import mate.academy.store.repository.user.UserRepository;
import mate.academy.store.service.shopping.cart.ShoppingCartService;
import mate.academy.store.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register new user with email: "
                + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName(RoleName.ROLE_USER)));
        userRepository.save(user);
        shoppingCartService.create(user);
        return userMapper.toResponseDto(user);
    }
}
