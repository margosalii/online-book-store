package mate.academy.store.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.UserLoginRequestDto;
import mate.academy.store.dto.UserLoginResponseDto;
import mate.academy.store.dto.UserRegistrationRequestDto;
import mate.academy.store.dto.UserResponseDto;
import mate.academy.store.exception.RegistrationException;
import mate.academy.store.security.AuthenticationService;
import mate.academy.store.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
