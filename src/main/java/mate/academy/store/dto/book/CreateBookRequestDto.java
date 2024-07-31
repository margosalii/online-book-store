package mate.academy.store.dto.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.store.validation.Isbn;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @Isbn
    private String isbn;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String description;

    private String coverImage;

    private Set<Long> categoryIds;
}
