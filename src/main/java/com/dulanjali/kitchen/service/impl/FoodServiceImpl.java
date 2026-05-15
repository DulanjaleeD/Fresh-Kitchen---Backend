package com.dulanjali.kitchen.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import com.dulanjali.kitchen.dao.CategoryDao;
import com.dulanjali.kitchen.dao.FoodItemDao;
import com.dulanjali.kitchen.dao.UserDao;
import com.dulanjali.kitchen.entities.FoodItem;
import com.dulanjali.kitchen.enums.FoodStatus;
import com.dulanjali.kitchen.enums.Role;
import com.dulanjali.kitchen.exception.ResourceNotFoundException;
import com.dulanjali.kitchen.exception.UnauthorizedActionException;
import com.dulanjali.kitchen.service.FoodService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private static final Logger log = LoggerFactory.getLogger(FoodServiceImpl.class);

    private final FoodItemDao foodItemDao;
    private final CategoryDao categoryDao;
    private final UserDao userDao;

    @Override
    public FoodItem createFood(String adminId, FoodItem foodItem) {
        validateAdmin(adminId);
        var category = categoryDao.findById(foodItem.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + foodItem.getCategory().getId()));

        if (foodItem.getStockQuantity() == null || foodItem.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity must be zero or greater");
        }

        foodItem.setCategory(category);
        updateAvailabilityStatus(foodItem);
        log.info("Creating food item '{}'", foodItem.getName());
        return foodItemDao.save(foodItem);
    }

    @Override
    public FoodItem updateFood(String adminId, Long foodId, FoodItem foodItem) {
        validateAdmin(adminId);

        if (foodItem.getStockQuantity() == null || foodItem.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity must be zero or greater");
        }

        FoodItem existing = getFoodById(foodId);
        existing.setName(foodItem.getName());
        existing.setDescription(foodItem.getDescription());
        existing.setPrice(foodItem.getPrice());
        existing.setStockQuantity(foodItem.getStockQuantity());

        if (foodItem.getCategory() != null && foodItem.getCategory().getId() != null) {
            existing.setCategory(categoryDao.findById(foodItem.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + foodItem.getCategory().getId())));
        }

        updateAvailabilityStatus(existing);
        log.info("Updating food item {}", foodId);
        return foodItemDao.save(existing);
    }

    @Override
    public void deleteFood(String adminId, Long foodId) {
        validateAdmin(adminId);
        FoodItem existing = getFoodById(foodId);
        log.info("Deleting food item {}", foodId);
        foodItemDao.delete(existing);
    }

    @Override
    public FoodItem getFoodById(Long foodId) {
        return foodItemDao.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found: " + foodId));
    }

    @Override
    public List<FoodItem> getAllFoods() {
        return foodItemDao.findAll();
    }

    @Override
    public List<FoodItem> getFoodsByCategory(Long categoryId) {
        return foodItemDao.findByCategoryId(categoryId);
    }

    private void validateAdmin(String userId) {
        var user = userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (user.getRole() != Role.ADMIN) {
            throw new UnauthorizedActionException("Only admins can perform this operation");
        }
    }

    private void updateAvailabilityStatus(FoodItem foodItem) {
        if (foodItem.getStockQuantity() > 0) {
            foodItem.setStatus(FoodStatus.AVAILABLE);
        } else {
            foodItem.setStatus(FoodStatus.OUT_OF_STOCK);
        }
    }
}
