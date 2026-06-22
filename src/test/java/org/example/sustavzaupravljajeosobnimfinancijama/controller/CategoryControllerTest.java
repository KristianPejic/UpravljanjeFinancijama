package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.CategoryResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.security.CustomUserDetails;
import org.example.sustavzaupravljajeosobnimfinancijama.security.CustomUserDetailsService;
import org.example.sustavzaupravljajeosobnimfinancijama.security.JwtTokenProvider;
import org.example.sustavzaupravljajeosobnimfinancijama.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private CustomUserDetails createUserDetails() {
        return new CustomUserDetails(1L, "testuser", "test@example.com", "password");
    }

    @Test
    void createCategory_ShouldReturn201() throws Exception {
        CategoryRequest request = new CategoryRequest("Food", TransactionType.EXPENSE);
        CategoryResponse response = new CategoryResponse(1L, "Food", TransactionType.EXPENSE);

        when(categoryService.createCategory(eq(1L), any(CategoryRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .with(user(createUserDetails()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Food"))
                .andExpect(jsonPath("$.type").value("EXPENSE"));
    }

    @Test
    void getAllCategories_ShouldReturn200() throws Exception {
        List<CategoryResponse> categories = Arrays.asList(
                new CategoryResponse(1L, "Food", TransactionType.EXPENSE),
                new CategoryResponse(2L, "Salary", TransactionType.INCOME));

        when(categoryService.getAllCategories(1L)).thenReturn(categories);

        mockMvc.perform(get("/api/categories")
                        .with(user(createUserDetails())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    void getCategoryById_ShouldReturn200() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "Food", TransactionType.EXPENSE);

        when(categoryService.getCategoryById(1L, 1L)).thenReturn(response);

        mockMvc.perform(get("/api/categories/1")
                        .with(user(createUserDetails())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    void unauthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());
    }
}
