package mate.academy.store.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}
