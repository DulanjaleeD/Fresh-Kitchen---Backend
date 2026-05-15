package com.dulanjali.kitchen.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dulanjali.kitchen.entities.Category;
import com.dulanjali.kitchen.securityConfig.CurrentUserProvider;
import com.dulanjali.kitchen.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        String adminId = currentUserProvider.getCurrentUserId();
        log.info("POST /categories by admin {}", adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(adminId, category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        String adminId = currentUserProvider.getCurrentUserId();
        log.info("PUT /categories/{} by admin {}", id, adminId);
        return ResponseEntity.ok(categoryService.updateCategory(adminId, id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        String adminId = currentUserProvider.getCurrentUserId();
        log.info("DELETE /categories/{} by admin {}", id, adminId);
        categoryService.deleteCategory(adminId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
