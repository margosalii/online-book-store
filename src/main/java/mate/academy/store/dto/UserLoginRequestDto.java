package mate.academy.store.dto;

import jakarta.validation.constraints.NotBlank;
import mate.academy.store.validation.Email;

public record UserLoginRequestDto(
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password) {
}
