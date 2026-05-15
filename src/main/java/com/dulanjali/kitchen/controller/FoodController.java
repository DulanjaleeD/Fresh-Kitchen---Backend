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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dulanjali.kitchen.entities.FoodItem;
import com.dulanjali.kitchen.securityConfig.CurrentUserProvider;
import com.dulanjali.kitchen.service.FoodService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
public class FoodController {

    private static final Logger log = LoggerFactory.getLogger(FoodController.class);

    private final FoodService foodService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<FoodItem> createFood(@RequestBody FoodItem foodItem) {
        String adminId = currentUserProvider.getCurrentUserId();
        log.info("POST /foods by admin {}", adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFood(adminId, foodItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFood(@PathVariable Long id, @RequestBody FoodItem foodItem) {
        String adminId = currentUserProvider.getCurrentUserId();
        log.info("PUT /foods/{} by admin {}", id, adminId);
        return ResponseEntity.ok(foodService.updateFood(adminId, id, foodItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        String adminId = currentUserProvider.getCurrentUserId();
        log.info("DELETE /foods/{} by admin {}", id, adminId);
        foodService.deleteFood(adminId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoods(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return ResponseEntity.ok(foodService.getFoodsByCategory(categoryId));
        }
        return ResponseEntity.ok(foodService.getAllFoods());
    }
}
