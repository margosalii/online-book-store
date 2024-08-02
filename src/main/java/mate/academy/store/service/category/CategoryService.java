package mate.academy.store.service.category;

import java.util.List;
import mate.academy.store.dto.category.CategoryDto;
import mate.academy.store.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryDto categoryDto);

    CategoryResponseDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
