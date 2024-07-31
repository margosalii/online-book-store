package mate.academy.store.dto.book;

import jakarta.validation.constraints.NotEmpty;

public record BookSearchParameters(@NotEmpty String[] authors,
                                   @NotEmpty String[] titles) {
}
