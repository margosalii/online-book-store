package mate.academy.store.controller;

import jakarta.persistence.EntityNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import mate.academy.store.dto.cart.item.CartItemDto;
import mate.academy.store.dto.cart.item.CartItemResponseDto;
import mate.academy.store.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.store.dto.shopping.cart.ShoppingCartDto;
import mate.academy.store.model.Book;
import mate.academy.store.model.CartItem;
import mate.academy.store.model.Category;
import mate.academy.store.model.Role;
import mate.academy.store.model.RoleName;
import mate.academy.store.model.ShoppingCart;
import mate.academy.store.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(
    scripts = {
        "classpath:database/categories/add-some-categories-to-table.sql",
        "classpath:database/books/add-some-books-to-table.sql",
        "classpath:database/categories/add-values-into-books-categories-table.sql",
        "classpath:database/shopping.carts/add-shopping-cart-to-table.sql",
        "classpath:database/cart.items/add-cart-item.sql"
    },
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Sql(
    scripts = {
        "classpath:database/cart.items/delete-cart-item.sql",
        "classpath:database/shopping.carts/delete-shopping-cart.sql",
        "classpath:database/categories/delete-categories-and-book-categories.sql",
        "classpath:database/categories/delete-categories.sql",
        "classpath:database/books/delete-books.sql"
    },
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    public static final Long ID = 1L;
    private static MockMvc mockMvc;
    private static ShoppingCartDto shoppingCartDto;
    private static ShoppingCart shoppingCart;
    private static CartItemResponseDto cartItemResponseDto;
    private static Book book;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
    }

    @BeforeEach
    void generateShoppingCartDto() {
        Role role = new Role();
        role.setId(ID);
        role.setName(RoleName.ROLE_ADMIN);

        User user = new User();
        user.setId(ID);
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setFirstName("first");
        user.setLastName("last");
        user.setRoles(Set.of(role));

        Category category = new Category();
        category.setId(ID);
        category.setName("Fantasy");

        book = new Book();
        book.setAuthor("J.K. Rowling");
        book.setTitle("Harry Potter");
        book.setPrice(BigDecimal.valueOf(10.99));
        book.setIsbn("9780747532699");
        book.setCategories(Set.of(category));

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setId(ID);
        cartItem.setQuantity(2);
        cartItem.setBook(book);

        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(ID);
        shoppingCart.setCartItems(Set.of(cartItem));

        cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(cartItem.getId());
        cartItemResponseDto.setBookId(ID);
        cartItemResponseDto.setQuantity(2);
        cartItemResponseDto.setBookTitle(cartItem.getBook().getTitle());

        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(ID);
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setCartItems(Set.of(cartItemResponseDto));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get shopping cart by ID")
    void getShoppingCartById_Ok() throws Exception {
        ShoppingCartDto expected = shoppingCartDto;

        MvcResult result = mockMvc.perform(
                get("/cart")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void addBookToTheCart_validRequest_Ok() throws Exception {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setBookId(ID);
        cartItemDto.setQuantity(15);

        CartItemResponseDto expected = new CartItemResponseDto();
        expected.setId(2L);
        expected.setQuantity(cartItemDto.getQuantity());
        expected.setBookId(cartItemDto.getBookId());
        expected.setBookTitle(book.getTitle());

        String request = objectMapper.writeValueAsString(cartItemDto);

        MvcResult result = mockMvc.perform(
                post("/cart")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateCartItemById_validRequest_Ok() throws Exception {
        UpdateCartItemRequestDto updRequestDto = new UpdateCartItemRequestDto();
        updRequestDto.setQuantity(5);

        cartItemResponseDto.setQuantity(updRequestDto.getQuantity());
        CartItemResponseDto expected = cartItemResponseDto;

        String request = objectMapper.writeValueAsString(updRequestDto);

        MvcResult result = mockMvc.perform(
                put("/cart/items/1")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteCartItemById_Ok() throws Exception {
        mockMvc.perform(
                delete("/cart/items/1"))
                .andExpect(status().isOk())
                .andReturn();
    }
}
