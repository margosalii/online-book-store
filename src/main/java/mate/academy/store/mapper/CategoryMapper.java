package mate.academy.store.mapper;

import mate.academy.store.config.MapperConfig;
import mate.academy.store.dto.category.CategoryDto;
import mate.academy.store.dto.category.CategoryResponseDto;
import mate.academy.store.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toResponseDto(Category category);

    Category toModel(CategoryDto categoryDto);

    void updateCategoryFromDto(CategoryDto requestDto, @MappingTarget Category category);
}
