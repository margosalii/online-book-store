package mate.academy.store.service.book.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.book.BookDto;
import mate.academy.store.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.store.dto.book.BookSearchParameters;
import mate.academy.store.dto.book.CreateBookRequestDto;
import mate.academy.store.mapper.BookMapper;
import mate.academy.store.model.Book;
import mate.academy.store.repository.book.BookRepository;
import mate.academy.store.repository.book.BookSpecificationBuilder;
import mate.academy.store.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Can't find book by id: " + id));
        bookMapper.updateBookFromDto(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoryId(id, pageable)
            .stream()
            .map(bookMapper::toBookWithoutCategoryId)
            .toList();
    }
}
