package mate.academy.store.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mate.academy.store.model.Status;

@Getter
@Setter
public class UpdateOrderRequestDto {
    @NotNull
    private Status status;
}
