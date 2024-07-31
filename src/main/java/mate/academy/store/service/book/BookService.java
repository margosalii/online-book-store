package mate.academy.store.service.book;

import java.util.List;
import mate.academy.store.dto.book.BookDto;
import mate.academy.store.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.store.dto.book.BookSearchParameters;
import mate.academy.store.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable);

    List<BookDtoWithoutCategoryIds> findByCategoryId(Long id, Pageable pageable);
}
