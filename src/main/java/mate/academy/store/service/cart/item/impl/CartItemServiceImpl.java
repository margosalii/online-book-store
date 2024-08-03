package mate.academy.store.service.cart.item.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.mapper.BookMapper;
import mate.academy.store.mapper.CartItemMapper;
import mate.academy.store.model.CartItem;
import mate.academy.store.model.ShoppingCart;
import mate.academy.store.repository.cart.item.CartItemRepository;
import mate.academy.store.service.book.BookService;
import mate.academy.store.service.cart.item.CartItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public CartItemResponseDto save(CartItemDto cartItemDto, ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toModel(cartItemDto);
        cartItem.setBook(bookMapper.toEntity(bookService.findById(cartItemDto.getBookId())));
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setShoppingCart(shoppingCart);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto getByCartIdAndItemId(Long id, Long shoppingCartId) {
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCartId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item with id: "
                    + id));
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
