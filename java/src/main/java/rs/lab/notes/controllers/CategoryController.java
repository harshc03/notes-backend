package rs.lab.notes.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.lab.notes.data.mapping.CategoryMapper;
import rs.lab.notes.data.model.Category;
import rs.lab.notes.dto.CategoryDto;
import rs.lab.notes.services.CategoryService;

@RestController
@Validated
@Tag(name = "Category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper mapper;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/categories.get/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) {
        var returnValue = categoryService.findCategoryById(id);
        return returnValue.isPresent()
                ? ResponseEntity.ok(mapper.toCategoryDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/categories.modify/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id, @Validated @RequestBody CategoryDto categoryDto) {
        var returnValue = categoryService.modifyCategory(id, categoryDto.getName());
        return returnValue.isPresent()
                ? ResponseEntity.ok(mapper.toCategoryDto(returnValue.get()))
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories.create")
    public ResponseEntity<?> createCategory(@Validated @RequestBody CategoryDto categoryDto) {
        var returnValue = categoryService.createCategory(categoryDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toCategoryDto(returnValue));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/categories.list")
    public ResponseEntity<?> categoryList(@ParameterObject Pageable pageable) {
        var returnValue = categoryService.listCategories(pageable);
        return ResponseEntity.ok(mapper.toPageDto(returnValue));
    }

    @GetMapping("/categories.search")
    public ResponseEntity<?> noteSearch(@RequestParam(value = "q", required = false) String query, @ParameterObject Pageable pageable) {
        Page<Category> returnValue;
        if (StringUtils.hasLength(query)) {
            returnValue = categoryService.searchByName(query, pageable);
        } else {
            returnValue = categoryService.listCategories(pageable);
        }
        return ResponseEntity.ok(mapper.toPageDto(returnValue));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/categories.delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
