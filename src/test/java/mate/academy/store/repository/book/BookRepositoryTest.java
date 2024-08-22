package mate.academy.store.repository.book;

import java.util.List;
import mate.academy.store.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = {
        "classpath:database/books/add-some-books-to-table.sql",
        "classpath:database/categories/add-some-categories-to-table.sql",
        "classpath:database/categories/add-values-into-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
        "classpath:database/categories/delete-categories-and-book-categories.sql",
        "classpath:database/books/delete-books.sql",
        "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ReturnsOneBook() {
        List<Book> actual = bookRepository
                .findAllByCategoryId(1L, PageRequest.of(0, 10));
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals("Harry Potter", actual.get(0).getTitle());
    }

    @Test
    @Sql(scripts = {
        "classpath:database/books/add-some-books-to-table.sql",
        "classpath:database/categories/add-some-categories-to-table.sql",
        "classpath:database/categories/add-values-into-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
        "classpath:database/categories/delete-categories-and-book-categories.sql",
        "classpath:database/books/delete-books.sql",
        "classpath:database/categories/delete-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_ReturnsTwoBooks() {
        List<Book> actual = bookRepository
                .findAllByCategoryId(2L, PageRequest.of(0, 10));
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("Harry Potter", actual.get(0).getTitle());
        Assertions.assertEquals("Treasure Island", actual.get(1).getTitle());
    }
}
