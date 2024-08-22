package mate.academy.store.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import mate.academy.store.dto.category.CategoryDto;
import mate.academy.store.dto.category.CategoryResponseDto;
import mate.academy.store.mapper.CategoryMapper;
import mate.academy.store.model.Category;
import mate.academy.store.repository.category.CategoryRepository;
import mate.academy.store.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    private static PageRequest pageRequest;
    private static CategoryDto categoryDto;
    private static CategoryDto updateDto;
    private static CategoryResponseDto responseDto;
    private static Category category;
    private static final Long ID = 1L;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @BeforeAll
    static void setUp() {
        categoryDto = new CategoryDto();
        categoryDto.setName("Adventure");
        categoryDto.setDescription("Adventurous books category");

        updateDto = new CategoryDto();
        updateDto.setName("Romantic");
        updateDto.setDescription("Romantic books category");

        category = new Category();
        category.setId(ID);
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        responseDto = new CategoryResponseDto();
        responseDto.setId(category.getId());
        responseDto.setName(category.getName());
        responseDto.setDescription(category.getDescription());

        pageRequest = PageRequest.of(0, 10);
    }

    @Test
    void findAllCategories_Ok() {
        when(categoryRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(category)));
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        List<CategoryResponseDto> actual = categoryService.findAll(pageRequest);
        List<CategoryResponseDto> expected = List.of(responseDto);

        assertEquals(expected, actual);
    }

    @Test
    void getCategoryByValidId_Ok() {
        CategoryResponseDto expected = responseDto;

        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto actual = categoryService.getById(ID);

        assertEquals(expected, actual);
    }

    @Test
    void getCategoryByInvalidId_ExceptionOk() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(ID));
    }

    @Test
    void saveCategory_validRequest_Ok() {
        when(categoryMapper.toModel(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto savedCategory = categoryService.save(categoryDto);

        assertEquals(responseDto, savedCategory);
    }

    @Test
    void updateCategoryByValidId_Ok() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto updated = categoryService.update(ID, updateDto);

        assertEquals(responseDto, updated);
        verify(categoryMapper, times(1)).updateCategoryFromDto(updateDto, category);
    }

    @Test
    void updateCategoryByInvalidId_ExceptionOk() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(ID, updateDto));
    }

    @Test
    void deleteCategoryById_Ok() {
        categoryService.deleteById(ID);
        verify(categoryRepository, times(1)).deleteById(ID);
    }
}
