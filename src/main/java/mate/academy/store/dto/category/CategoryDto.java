package mate.academy.store.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
