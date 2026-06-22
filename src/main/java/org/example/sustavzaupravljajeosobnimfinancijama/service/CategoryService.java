package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(Long userId, CategoryRequest request);

    List<CategoryResponse> getAllCategories(Long userId);

    List<CategoryResponse> getCategoriesByType(Long userId, TransactionType type);

    CategoryResponse getCategoryById(Long userId, Long categoryId);

    CategoryResponse updateCategory(Long userId, Long categoryId, CategoryRequest request);

    void deleteCategory(Long userId, Long categoryId);
}
