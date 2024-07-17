package mate.academy.store.service;

import java.util.List;
import mate.academy.store.dto.BookDto;
import mate.academy.store.dto.BookSearchParameters;
import mate.academy.store.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);
}
