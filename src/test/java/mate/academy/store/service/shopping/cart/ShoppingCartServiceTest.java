package mate.academy.store.service.shopping.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.store.dto.shopping.cart.ShoppingCartDto;
import mate.academy.store.mapper.CartItemMapper;
import mate.academy.store.mapper.ShoppingCartMapper;
import mate.academy.store.model.Book;
import mate.academy.store.model.CartItem;
import mate.academy.store.model.Category;
import mate.academy.store.model.Role;
import mate.academy.store.model.RoleName;
import mate.academy.store.model.ShoppingCart;
import mate.academy.store.model.User;
import mate.academy.store.repository.cart.item.CartItemRepository;
import mate.academy.store.repository.shopping.cart.ShoppingCartRepository;
import mate.academy.store.service.cart.item.CartItemService;
import mate.academy.store.service.shopping.cart.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    private static User user;
    private static ShoppingCart shoppingCart;
    private static ShoppingCartDto shoppingCartDto;
    private static CartItem cartItem;
    private static CartItemDto cartItemDto;
    private static CartItemResponseDto cartItemResponseDto;
    private static UpdateCartItemRequestDto updateCartItemRequestDto;
    private static final Long ID = 1L;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void setUp() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.ROLE_ADMIN);

        Category category = new Category();
        category.setId(1L);
        category.setName("Fantasy");

        Book book = new Book();
        book.setAuthor("J.K. Rowling");
        book.setTitle("Harry Potter");
        book.setPrice(BigDecimal.valueOf(10.99));
        book.setIsbn("9780747532699");
        book.setCategories(Set.of(category));

        user = new User();
        user.setId(1L);
        user.setEmail("useradmin@gmail.com");
        user.setPassword("password1234");
        user.setFirstName("User");
        user.setLastName("Admin");
        user.setRoles(Set.of(role));

        cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setId(1L);
        cartItem.setQuantity(2);
        cartItem.setBook(book);

        cartItemDto = new CartItemDto();
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setBookId(cartItem.getBook().getId());

        cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(cartItem.getId());
        cartItemResponseDto.setBookId(cartItemDto.getBookId());
        cartItemResponseDto.setQuantity(cartItemDto.getQuantity());
        cartItemResponseDto.setBookTitle(cartItem.getBook().getTitle());

        updateCartItemRequestDto = new UpdateCartItemRequestDto();
        updateCartItemRequestDto.setQuantity(4);

        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(1L);
        shoppingCart.setCartItems(Set.of(cartItem));

        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setUserId(shoppingCart.getUser().getId());
        shoppingCartDto.setCartItems(Set.of(cartItemResponseDto));
    }

    @Test
    void createShoppingCart_Ok() {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);

        when(shoppingCartRepository.save(newShoppingCart)).thenReturn(shoppingCart);

        ShoppingCart savedShoppingCart = shoppingCartService.create(user);

        assertEquals(shoppingCart, savedShoppingCart);
    }

    @Test
    void getShoppingCart_byValidId_Ok() {
        when(shoppingCartRepository.findByUserId(ID)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemResponseDto);

        ShoppingCartDto expected = shoppingCartDto;

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(ID);

        assertEquals(expected, actual);
    }

    @Test
    void getShoppingCart_byInvalidId_ExceptionOk() {
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService.getShoppingCart(ID));

    }

    @Test
    void addBookToTheShoppingCart_byValidId_Ok() {
        when(shoppingCartRepository.findByUserId(ID)).thenReturn(Optional.of(shoppingCart));
        when(cartItemService.save(cartItemDto, shoppingCart)).thenReturn(cartItemResponseDto);

        CartItemResponseDto addedBook = shoppingCartService
                .addBookToTheShoppingCart(ID, cartItemDto);

        assertEquals(cartItemResponseDto, addedBook);
    }

    @Test
    void addBookToTheShoppingCart_byInvalidId_ExceptionOk() {
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addBookToTheShoppingCart(ID, cartItemDto));
    }

    @Test
    void updateItemInCart_ValidRequest_Ok() {
        when(shoppingCartRepository.findByUserId(ID)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(ID, ID))
                .thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemResponseDto);

        CartItemResponseDto expected = cartItemResponseDto;

        CartItemResponseDto actual = shoppingCartService
                .updateItemInCart(ID, ID, updateCartItemRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    void updateItemInCart_InvalidUserId_ExceptionOk() {
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService
                .updateItemInCart(ID, ID, updateCartItemRequestDto));
    }

    @Test
    void updateItemInCart_InvalidItemId_ExceptionOk() {
        when(shoppingCartRepository.findByUserId(ID)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByIdAndShoppingCartId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService
                .updateItemInCart(ID, ID, updateCartItemRequestDto));
    }

    @Test
    void deleteItemFromCart() {
        when(shoppingCartRepository.findByUserId(ID)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemResponseDto);
        when(cartItemService.getByCartIdAndItemId(ID, shoppingCartDto.getId()))
                .thenReturn(cartItemResponseDto);

        shoppingCartService.deleteItemFromCart(ID, ID);

        verify(cartItemService, times(1)).deleteById(ID);
    }
}
