package mate.academy.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.store.dto.category.CategoryDto;
import mate.academy.store.dto.category.CategoryResponseDto;
import mate.academy.store.service.book.BookService;
import mate.academy.store.service.category.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new category",
            description = "Create a new category and save it to the DB")
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all categories", description = "Get a list of all categories")
    public List<CategoryResponseDto> getAll(@ParameterObject @PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Get one category according to its ID")
    public CategoryResponseDto getCategoryById(@PathVariable @Positive Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category",
            description = "Update an existing category from DB by its ID")
    public CategoryResponseDto updateCategory(@PathVariable @Positive Long id,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category",
            description = "Delete one category from DB according to its ID")
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by categoryIds",
            description = "Get list of books by specific category ID")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable @Positive Long id,
            @ParameterObject @PageableDefault Pageable pageable) {
        return bookService.findByCategoryId(id, pageable);
    }
}
