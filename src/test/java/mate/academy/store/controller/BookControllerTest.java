package mate.academy.store.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mate.academy.store.dto.book.BookDto;
import mate.academy.store.dto.book.CreateBookRequestDto;
import mate.academy.store.model.Book;
import mate.academy.store.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    private static MockMvc mockMvc;
    private static List<BookDto> bookDtos;
    private static final String DTO_TITLE = "Kobzar";
    private static final String DTO_AUTHOR = "Shevchenko";
    private static final String DTO_DESCRIPTION = "Kobzar by Shevchenko";
    private static final String DTO_ISBN = "9780141321002";
    private static final BigDecimal DTO_PRICE = BigDecimal.valueOf(16);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository repository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
        generateBookDtos();
    }

    private static void generateBookDtos() {
        bookDtos = new ArrayList<>();
        bookDtos.add(
            new BookDto()
                .setId(1L)
                .setTitle("Harry Potter")
                .setAuthor("J.K. Rowling")
                .setIsbn("9780747532699")
                .setPrice(BigDecimal.valueOf(11))
                .setCategoryIds(Collections.emptySet()));
        bookDtos.add(
            new BookDto()
                .setId(2L)
                .setTitle("Treasure Island")
                .setAuthor("Robert Louis Stevenson")
                .setIsbn("9780141321004")
                .setPrice(BigDecimal.valueOf(8))
                .setCategoryIds(Collections.emptySet()));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/delete-kobzar-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new book")
    void createBook_validRequestDto_Ok() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(DTO_AUTHOR);
        requestDto.setTitle(DTO_TITLE);
        requestDto.setDescription(DTO_DESCRIPTION);
        requestDto.setPrice(DTO_PRICE);
        requestDto.setIsbn(DTO_ISBN);

        BookDto expected = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setPrice(requestDto.getPrice())
                .setIsbn(requestDto.getIsbn());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/books")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-some-books-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get all books")
    void getAllBooks_Ok() throws Exception {
        List<BookDto> expected = bookDtos;

        MvcResult result = mockMvc.perform(
                get("/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), BookDto[].class);

        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.asList(actual));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-some-books-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get book by ID")
    void getBookById_Ok() throws Exception {
        BookDto expected = bookDtos.get(0);
        MvcResult result = mockMvc.perform(
                get("/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-some-books-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update book by ID")
    void updateBook_validIdAndRequestDto_Ok() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(DTO_AUTHOR);
        requestDto.setTitle(DTO_TITLE);
        requestDto.setDescription(DTO_DESCRIPTION);
        requestDto.setPrice(DTO_PRICE);
        requestDto.setIsbn(DTO_ISBN);

        BookDto expected = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setPrice(requestDto.getPrice())
                .setIsbn(requestDto.getIsbn());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                put("/books/1")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-some-books-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Delete book by ID")
    void deleteBookById_Ok() throws Exception {
        mockMvc.perform(
                delete("/books/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Optional<Book> deletedBook = repository.findById(1L);
        Assertions.assertFalse(deletedBook.isPresent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-some-books-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Search book by parameters")
    void searchBookByParameters_Ok() throws Exception {
        List<BookDto> expected = List.of(bookDtos.get(0));

        MvcResult result = mockMvc.perform(
                get("/books/search?titles=Harry Potter&authors=J.K. Rowling")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper.readValue(result
            .getResponse()
            .getContentAsByteArray(), BookDto[].class);

        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }
}
