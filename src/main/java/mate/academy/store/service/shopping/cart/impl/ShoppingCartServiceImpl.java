package mate.academy.store.service.shopping.cart.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.store.dto.shopping.cart.ShoppingCartDto;
import mate.academy.store.mapper.CartItemMapper;
import mate.academy.store.mapper.ShoppingCartMapper;
import mate.academy.store.model.CartItem;
import mate.academy.store.model.ShoppingCart;
import mate.academy.store.model.User;
import mate.academy.store.repository.cart.item.CartItemRepository;
import mate.academy.store.repository.shopping.cart.ShoppingCartRepository;
import mate.academy.store.service.cart.item.CartItemService;
import mate.academy.store.service.shopping.cart.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCart create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find users shopping cart")
        );
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);

        shoppingCartDto.setCartItems(
                shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet())
        );
        return shoppingCartDto;
    }

    @Override
    public CartItemResponseDto addBookToTheShoppingCart(Long userId, CartItemDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user's shopping cart")
        );
        return cartItemService.save(requestDto, shoppingCart);
    }

    @Override
    public CartItemResponseDto updateItemInCart(Long userId,
                                                Long id,
                                                UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user's shopping cart")
        );
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCartId(id, shoppingCart.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find cart item with id: " + id));
        cartItemMapper.updateCartItemFromDto(requestDto, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteItemFromCart(Long userId, Long id) {
        ShoppingCartDto shoppingCart = getShoppingCart(userId);
        CartItemResponseDto cartItem = cartItemService
                .getByCartIdAndItemId(id, shoppingCart.getId());
        cartItemService.deleteById(cartItem.getId());
    }
}
