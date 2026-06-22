package org.example.sustavzaupravljajeosobnimfinancijama.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.exception.ResourceNotFoundException;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Category;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.model.User;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.CategoryRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.UserRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public CategoryResponse createCategory(Long userId, CategoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setUser(user);

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
    }

    @Override
    public List<CategoryResponse> getAllCategories(Long userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoriesByType(Long userId, TransactionType type) {
        return categoryRepository.findByUserIdAndType(userId, type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long userId, Long categoryId) {
        Category category = findCategoryByIdAndUserId(categoryId, userId);
        return mapToResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Long userId, Long categoryId, CategoryRequest request) {
        Category category = findCategoryByIdAndUserId(categoryId, userId);
        category.setName(request.getName());
        category.setType(request.getType());

        Category updated = categoryRepository.save(category);
        return mapToResponse(updated);
    }

    @Override
    public void deleteCategory(Long userId, Long categoryId) {
        Category category = findCategoryByIdAndUserId(categoryId, userId);
        categoryRepository.delete(category);
    }

    private Category findCategoryByIdAndUserId(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        return category;
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }
}
