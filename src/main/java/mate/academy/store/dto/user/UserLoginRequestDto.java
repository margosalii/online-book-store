package mate.academy.store.dto.user;

import jakarta.validation.constraints.NotBlank;
import mate.academy.store.validation.Email;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password) {
}
