package mate.academy.store.mapper;

import mate.academy.store.config.MapperConfig;
import mate.academy.store.dto.BookDto;
import mate.academy.store.dto.CreateBookRequestDto;
import mate.academy.store.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);
}
