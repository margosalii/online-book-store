package mate.academy.store.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.store.dto.book.BookDto;
import mate.academy.store.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.store.dto.book.BookSearchParameters;
import mate.academy.store.dto.book.CreateBookRequestDto;
import mate.academy.store.mapper.BookMapper;
import mate.academy.store.model.Book;
import mate.academy.store.model.Category;
import mate.academy.store.repository.book.BookRepository;
import mate.academy.store.repository.book.BookSpecificationBuilder;
import mate.academy.store.service.book.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private static CreateBookRequestDto requestDto;
    private static CreateBookRequestDto updateRequestDto;
    private static Book book;
    private static BookDto bookDto;
    private static PageRequest pageRequest;
    private static final Long ID = 1L;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void setUp() {
        Category category = new Category();
        category.setId(ID);
        category.setName("Fantasy");

        pageRequest = PageRequest.of(0, 10);

        requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("J.K. Rowling");
        requestDto.setTitle("Harry Potter");
        requestDto.setPrice(BigDecimal.valueOf(10.99));
        requestDto.setIsbn("9780747532699");
        requestDto.setCategoryIds(Set.of(category.getId()));

        updateRequestDto = new CreateBookRequestDto();
        updateRequestDto.setAuthor("Sapkovskii");
        updateRequestDto.setTitle("The Witcher");
        updateRequestDto.setPrice(BigDecimal.valueOf(11.99));
        updateRequestDto.setIsbn("9780747532699");
        updateRequestDto.setCategoryIds(Set.of(category.getId()));

        book = new Book();
        book.setId(ID);
        book.setAuthor(requestDto.getAuthor());
        book.setTitle(requestDto.getTitle());
        book.setPrice(requestDto.getPrice());
        book.setIsbn(requestDto.getIsbn());
        book.setCategories(new HashSet<>(Set.of(category)));

        bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setPrice(book.getPrice());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCategoryIds(Set.of(category.getId()));
    }

    @Test
    void saveBook_validRequest_Ok() {
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBook = bookService.save(requestDto);

        assertEquals(bookDto, savedBook);
    }

    @Test
    void findAllBooks_Ok() {
        when(bookRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.findAll(pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void findBookById_Ok() {
        BookDto expected = bookDto;

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actualBook = bookService.findById(ID);
        assertEquals(expected, actualBook);
    }

    @Test
    void findBookByInvalidId_ExceptionOk() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.findById(ID));
    }

    @Test
    void updateBookById_Ok() {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto expected = bookDto;

        BookDto actualBook = bookService.updateById(ID, updateRequestDto);
        assertEquals(expected, actualBook);
    }

    @Test
    void updateBookByInvalidId_ExceptionOk() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(ID, updateRequestDto));
    }

    @Test
    void deleteBookById_Ok() {
        bookService.deleteById(ID);
        verify(bookRepository, times(1)).deleteById(ID);
    }

    @Test
    void searchBooksByValidParams_Ok() {
        String[] authors = new String[]{"J.K"};
        String[] titles = new String[]{"Harry"};

        Page<Book> page = new PageImpl<>(List.of(book));

        BookSearchParameters searchParameters = new BookSearchParameters(authors, titles);
        Specification<Book> bookSpecification = specificationBuilder.build(searchParameters);

        when(bookRepository.findAll(bookSpecification, pageRequest)).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> expected = List.of(bookDto);
        List<BookDto> actual = bookService.search(searchParameters, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void findBooksByCategoryIds_Ok() {
        BookDtoWithoutCategoryIds dtosWithoutCategoryIds = new BookDtoWithoutCategoryIds();

        when(bookRepository.findAllByCategoryId(1L, pageRequest))
                .thenReturn(List.of(book));
        when(bookMapper.toBookWithoutCategoryId(book)).thenReturn(dtosWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> actual = bookService.findByCategoryId(1L, pageRequest);

        assertEquals(List.of(dtosWithoutCategoryIds), actual);
    }
}
