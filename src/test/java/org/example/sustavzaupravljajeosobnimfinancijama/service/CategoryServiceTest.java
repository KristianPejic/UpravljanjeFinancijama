package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.exception.ResourceNotFoundException;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Category;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.model.User;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.CategoryRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.UserRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");

        category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setType(TransactionType.EXPENSE);
        category.setUser(user);
    }

    @Test
    void createCategory_ShouldReturnCategoryResponse() {
        CategoryRequest request = new CategoryRequest("Food", TransactionType.EXPENSE);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponse response = categoryService.createCategory(1L, request);

        assertNotNull(response);
        assertEquals("Food", response.getName());
        assertEquals(TransactionType.EXPENSE, response.getType());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_UserNotFound_ShouldThrowException() {
        CategoryRequest request = new CategoryRequest("Food", TransactionType.EXPENSE);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.createCategory(1L, request));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Salary");
        category2.setType(TransactionType.INCOME);
        category2.setUser(user);

        when(categoryRepository.findByUserId(1L)).thenReturn(Arrays.asList(category, category2));

        List<CategoryResponse> responses = categoryService.getAllCategories(1L);

        assertEquals(2, responses.size());
        assertEquals("Food", responses.get(0).getName());
        assertEquals("Salary", responses.get(1).getName());
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.getCategoryById(1L, 1L);

        assertNotNull(response);
        assertEquals("Food", response.getName());
    }

    @Test
    void getCategoryById_NotFound_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L, 1L));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() {
        CategoryRequest request = new CategoryRequest("Groceries", TransactionType.EXPENSE);
        Category updated = new Category();
        updated.setId(1L);
        updated.setName("Groceries");
        updated.setType(TransactionType.EXPENSE);
        updated.setUser(user);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);

        CategoryResponse response = categoryService.updateCategory(1L, 1L, request);

        assertEquals("Groceries", response.getName());
    }

    @Test
    void deleteCategory_ShouldCallDelete() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L, 1L);

        verify(categoryRepository).delete(category);
    }
}
