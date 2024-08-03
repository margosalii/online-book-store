package mate.academy.store.dto.shopping.cart;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.store.dto.cart.item.CartItemResponseDto;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
