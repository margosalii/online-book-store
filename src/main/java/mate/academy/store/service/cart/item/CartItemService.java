package mate.academy.store.service.cart.item;

import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.model.ShoppingCart;

public interface CartItemService {
    CartItemResponseDto save(CartItemDto cartItemDto, ShoppingCart shoppingCart);

    CartItemResponseDto getByCartIdAndItemId(Long id, Long shoppingCartId);

    void deleteById(Long id);
}
