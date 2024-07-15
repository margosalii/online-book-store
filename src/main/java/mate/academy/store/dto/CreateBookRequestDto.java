package mate.academy.store.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import mate.academy.store.validation.Isbn;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBookRequestDto {
    @NotNull
    private String title;

    @NotNull
    private String author;

    @Isbn
    private String isbn;

    @NotNull
    @Min(0)
    private BigDecimal price;

    private String description;
    private String coverImage;
}
