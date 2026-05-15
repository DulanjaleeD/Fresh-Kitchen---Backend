package com.dulanjali.kitchen.service;

import java.util.List;

import com.dulanjali.kitchen.entities.FoodItem;

public interface FoodService {
    FoodItem createFood(String adminId, FoodItem foodItem);
    FoodItem updateFood(String adminId, Long foodId, FoodItem foodItem);
    void deleteFood(String adminId, Long foodId);
    FoodItem getFoodById(Long foodId);
    List<FoodItem> getAllFoods();
    List<FoodItem> getFoodsByCategory(Long categoryId);
}
