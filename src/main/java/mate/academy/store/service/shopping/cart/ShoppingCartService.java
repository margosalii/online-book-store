package mate.academy.store.service.shopping.cart;

import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.store.dto.shopping.cart.ShoppingCartDto;
import mate.academy.store.model.ShoppingCart;
import mate.academy.store.model.User;

public interface ShoppingCartService {
    ShoppingCart create(User user);

    ShoppingCartDto getShoppingCart(Long id);

    CartItemResponseDto addBookToTheShoppingCart(Long id, CartItemDto requestDto);

    CartItemResponseDto updateItemInCart(Long userId, Long id, UpdateCartItemRequestDto requestDto);

    void deleteItemFromCart(Long userId, Long id);
}
