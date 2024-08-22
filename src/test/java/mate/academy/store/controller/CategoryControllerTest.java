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
import java.util.List;
import java.util.Optional;
import mate.academy.store.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.store.dto.category.CategoryDto;
import mate.academy.store.dto.category.CategoryResponseDto;
import mate.academy.store.model.Category;
import mate.academy.store.repository.category.CategoryRepository;
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
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static List<CategoryResponseDto> categoryDtos;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository repository;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
        generateCategoryDtos();
    }

    private static void generateCategoryDtos() {
        categoryDtos = new ArrayList<>();
        categoryDtos.add(new CategoryResponseDto()
                .setName("Fantasy").setId(1L));
        categoryDtos.add(new CategoryResponseDto()
                .setName("Adventure").setId(2L));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new category")
    void createCategory_validRequestDto_Ok() throws Exception {
        CategoryDto requestDto = new CategoryDto();
        requestDto.setName("Horror");
        requestDto.setDescription("Horror books");

        CategoryResponseDto expected = new CategoryResponseDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription()).setId(1L);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/categories")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get all categories")
    void getAllCategories_Ok() throws Exception {
        List<CategoryResponseDto> expected = categoryDtos;

        MvcResult result = mockMvc.perform(
                get("/categories").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto[] actual = objectMapper.readValue(result
            .getResponse()
            .getContentAsByteArray(), CategoryResponseDto[].class);

        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get category by ID")
    void getCategoryById_Ok() throws Exception {
        CategoryResponseDto expected = categoryDtos.get(0);

        MvcResult result = mockMvc.perform(
                get("/categories/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), CategoryResponseDto.class);

        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update category by ID")
    void updateCategoryById_Ok() throws Exception {
        CategoryDto requestDto = new CategoryDto()
                .setName("Romantic").setDescription("Romantic books");

        CategoryResponseDto expected = new CategoryResponseDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                put("/categories/1")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsByteArray(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/categories/add-some-categories-to-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/delete-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Delete category by ID")
    void deleteCategoryById_Ok() throws Exception {
        mockMvc.perform(
                delete("/categories/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Category> deletedCategory = repository.findById(1L);
        Assertions.assertFalse(deletedCategory.isPresent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {
                "classpath:database/books/add-some-books-to-table.sql",
                "classpath:database/categories/add-some-categories-to-table.sql",
                "classpath:database/categories/add-values-into-books-categories-table.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                "classpath:database/categories/delete-categories-and-book-categories.sql",
                "classpath:database/categories/delete-categories.sql",
                "classpath:database/books/delete-books.sql",
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Get book by category ID")
    void getBookByCategoryId_Ok() throws Exception {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(1L);
        bookDto.setTitle("Harry Potter");
        bookDto.setAuthor("J.K. Rowling");
        bookDto.setIsbn("9780747532699");
        bookDto.setPrice(BigDecimal.valueOf(11));

        List<BookDtoWithoutCategoryIds> expected = List.of(bookDto);

        MvcResult result = mockMvc.perform(
                get("/categories/1/books").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(result.getResponse()
            .getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);

        Assertions.assertEquals(1, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }
}
