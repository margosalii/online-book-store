package mate.academy.store.dto;

import jakarta.validation.constraints.NotEmpty;

public record BookSearchParameters(@NotEmpty String[] authors,
                                   @NotEmpty String[] titles) {
}
