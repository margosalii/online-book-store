package mate.academy.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.store.dto.shopping.cart.ShoppingCartDto;
import mate.academy.store.model.User;
import mate.academy.store.service.shopping.cart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Validated
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get users shopping cart", description = "Get a content of shopping cart")
    public ShoppingCartDto getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((User) authentication.getPrincipal()).getId();
        return shoppingCartService.getShoppingCart(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Add book to shopping cart",
            description = "Add existing book from DB to users shopping cart")
    public CartItemResponseDto addBookToTheCart(@RequestBody @Valid CartItemDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((User) authentication.getPrincipal()).getId();
        return shoppingCartService.addBookToTheShoppingCart(id, requestDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/items/{id}")
    @Operation(summary = "Update cart item",
            description = "Update quantity of one specific book in the cart")
    public CartItemResponseDto updateCartItem(@Positive @PathVariable Long id,
                                      @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        return shoppingCartService.updateItemInCart(userId, id, requestDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete cart item",
            description = "Delete cart item from users shopping cart")
    public void deleteCartItem(@Positive @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((User) authentication.getPrincipal()).getId();
        shoppingCartService.deleteItemFromCart(userId, id);
    }
}
