package mate.academy.store.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOrderDto {
    @NotBlank
    private String shippingAddress;
}
