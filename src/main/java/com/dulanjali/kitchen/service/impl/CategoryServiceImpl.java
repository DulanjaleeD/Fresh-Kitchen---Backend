package com.dulanjali.kitchen.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.CategoryDao;
import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.entities.Category;
import com.dulanjali.kitchen.enums.Role;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import com.dulanjali.kitchen.exception.UnauthorizedActionException;
import com.dulanjali.kitchen.service.CategoryService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryDao categoryDao;
    private final UserDao userDao;

    @Override
    public Category createCategory(String adminId, Category category) {
        validateAdmin(adminId);
        log.info("Creating category '{}'", category.getName());
        return categoryDao.save(category);
    }

    @Override
    public Category updateCategory(String adminId, Long categoryId, Category category) {
        validateAdmin(adminId);
        Category existing = getCategoryById(categoryId);
        existing.setName(category.getName());
        log.info("Updating category {}", categoryId);
        return categoryDao.save(existing);
    }

    @Override
    public void deleteCategory(String adminId, Long categoryId) {
        validateAdmin(adminId);
        Category existing = getCategoryById(categoryId);
        log.info("Deleting category {}", categoryId);
        categoryDao.delete(existing);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    private void validateAdmin(String userId) {
        var user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("Only admins can perform this operation");
        }
    }
}
