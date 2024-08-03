package mate.academy.store.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.store.config.MapperConfig;
import mate.academy.store.dto.book.BookDto;
import mate.academy.store.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.store.dto.book.CreateBookRequestDto;
import mate.academy.store.model.Book;
import mate.academy.store.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(BookDto book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toBookWithoutCategoryId(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> ids = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(ids);
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookRequestDto) {
        if (bookRequestDto.getCategoryIds() != null) {
            book.setCategories(bookRequestDto
                    .getCategoryIds()
                    .stream()
                    .map(Category::new)
                    .collect(Collectors.toSet()));
        }
    }
}
