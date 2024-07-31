package mate.academy.store.service.category.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.category.CategoryDto;
import mate.academy.store.dto.category.CategoryResponseDto;
import mate.academy.store.mapper.CategoryMapper;
import mate.academy.store.model.Category;
import mate.academy.store.repository.category.CategoryRepository;
import mate.academy.store.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
            .stream()
            .map(categoryMapper::toResponseDto)
            .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toModel(categoryDto);
        return categoryMapper.toResponseDto(categoryRepository.save(category));

    }

    @Override
    public CategoryResponseDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id));
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
