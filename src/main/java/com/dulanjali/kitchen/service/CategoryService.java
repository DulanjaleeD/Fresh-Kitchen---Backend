package com.dulanjali.kitchen.service;

import java.util.List;

import com.dulanjali.kitchen.entities.Category;

public interface CategoryService {
    Category createCategory(String adminId, Category category);
    Category updateCategory(String adminId, Long categoryId, Category category);
    void deleteCategory(String adminId, Long categoryId);
    Category getCategoryById(Long categoryId);
    List<Category> getAllCategories();
}
