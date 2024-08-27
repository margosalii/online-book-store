package mate.academy.store.dto.shopping.cart;

import java.util.Set;
import lombok.Data;
import mate.academy.store.dto.cart.item.CartItemResponseDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
