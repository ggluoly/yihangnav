package com.yihangnav.core.service;

import com.yihangnav.core.domain.Category;
import com.yihangnav.core.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> listAll() {
        return categoryRepository.findAllByOrderBySortOrderAscIdAsc();
    }

    public List<Category> listRoot() {
        return categoryRepository.findAllByParentIsNullOrderBySortOrderAscIdAsc();
    }

    public List<Category> listByParent(Long parentId) {
        return categoryRepository.findAllByParentIdOrderBySortOrderAscIdAsc(parentId);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
