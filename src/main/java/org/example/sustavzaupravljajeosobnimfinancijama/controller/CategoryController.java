package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) TransactionType type) {
        List<CategoryResponse> categories;
        if (type != null) {
            categories = categoryService.getCategoriesByType(userId, type);
        } else {
            categories = categoryService.getAllCategories(userId);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        categoryService.deleteCategory(userId, id);
        return ResponseEntity.noContent().build();
    }
}
