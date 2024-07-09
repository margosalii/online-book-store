package mate.academy.store.service;

import java.util.List;
import mate.academy.store.dto.BookDto;
import mate.academy.store.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void updateById(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
