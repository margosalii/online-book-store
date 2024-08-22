package mate.academy.store.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CategoryDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
