package com.yihangnav.web.controller;

import com.yihangnav.core.domain.Category;
import com.yihangnav.core.service.CategoryService;
import com.yihangnav.web.dto.ApiResponse;
import com.yihangnav.web.dto.CategoryAdminDTO;
import com.yihangnav.web.dto.CategoryUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<CategoryAdminDTO>> listAll() {
        return ApiResponse.ok(categoryService.listAll().stream()
                .map(CategoryAdminDTO::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}/children")
    public ApiResponse<List<Category>> listChildren(@PathVariable Long id) {
        return ApiResponse.ok(categoryService.listByParent(id));
    }

    @PostMapping
    public ApiResponse<CategoryAdminDTO> create(@Valid @RequestBody Category category) {
        Category saved = categoryService.save(category);
        return ApiResponse.ok(CategoryAdminDTO.from(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryAdminDTO>> update(@PathVariable Long id, @RequestBody CategoryUpdateRequest request) {
        return categoryService.findById(id)
                .map(existing -> {
                    if (request.getName() != null) {
                        existing.setName(request.getName());
                    }
                    if (request.getDescription() != null) {
                        existing.setDescription(request.getDescription());
                    }
                    if (request.getIcon() != null) {
                        existing.setIcon(request.getIcon());
                    }
                    if (request.getSortOrder() != null) {
                        existing.setSortOrder(request.getSortOrder());
                    }
                    if (request.isParentIdSet()) {
                        if (request.getParentId() != null) {
                            Category parent = new Category();
                            parent.setId(request.getParentId());
                            existing.setParent(parent);
                        } else {
                            existing.setParent(null);
                        }
                    }
                    Category saved = categoryService.save(existing);
                    return ResponseEntity.ok(ApiResponse.ok(CategoryAdminDTO.from(saved)));
                })
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.<CategoryAdminDTO>fail("category not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok(null));
    }
}
