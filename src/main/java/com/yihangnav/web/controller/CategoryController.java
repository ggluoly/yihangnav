package com.yihangnav.web.controller;

import com.yihangnav.core.domain.Category;
import com.yihangnav.core.service.CategoryService;
import com.yihangnav.web.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<Category>> listRoot() {
        return ApiResponse.ok(categoryService.listRoot());
    }

    @GetMapping("/{id}/children")
    public ApiResponse<List<Category>> listChildren(@PathVariable Long id) {
        return ApiResponse.ok(categoryService.listByParent(id));
    }

    @PostMapping
    public ApiResponse<Category> create(@Valid @RequestBody Category category) {
        return ApiResponse.ok(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> update(@PathVariable Long id, @Valid @RequestBody Category category) {
        return categoryService.findById(id)
                .map(existing -> {
                    category.setId(existing.getId());
                    return ResponseEntity.ok(ApiResponse.ok(categoryService.save(category)));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
