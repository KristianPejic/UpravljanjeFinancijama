package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.security.CustomUserDetails;
import org.example.sustavzaupravljajeosobnimfinancijama.security.CustomUserDetailsService;
import org.example.sustavzaupravljajeosobnimfinancijama.security.JwtTokenProvider;
import org.example.sustavzaupravljajeosobnimfinancijama.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private CustomUserDetails createUserDetails() {
        return new CustomUserDetails(1L, "testuser", "test@example.com", "password");
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createTransaction_ShouldReturn201() throws Exception {
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("50.00"), "Lunch", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, 1L);

        TransactionResponse response = new TransactionResponse(
                1L, new BigDecimal("50.00"), "Lunch", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, "Food", 1L, LocalDateTime.now());

        when(transactionService.createTransaction(eq(1L), any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                        .with(user(createUserDetails()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(50.00))
                .andExpect(jsonPath("$.description").value("Lunch"));
    }

    @Test
    void getAllTransactions_ShouldReturn200() throws Exception {
        List<TransactionResponse> transactions = Arrays.asList(
                new TransactionResponse(1L, new BigDecimal("50.00"), "Lunch",
                        LocalDate.of(2026, 6, 15), TransactionType.EXPENSE, "Food", 1L, LocalDateTime.now()),
                new TransactionResponse(2L, new BigDecimal("3000.00"), "Salary",
                        LocalDate.of(2026, 6, 1), TransactionType.INCOME, "Salary", 5L, LocalDateTime.now()));

        when(transactionService.getAllTransactions(1L)).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions")
                        .with(user(createUserDetails())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTransactionById_ShouldReturn200() throws Exception {
        TransactionResponse response = new TransactionResponse(
                1L, new BigDecimal("50.00"), "Lunch", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, "Food", 1L, LocalDateTime.now());

        when(transactionService.getTransactionById(1L, 1L)).thenReturn(response);

        mockMvc.perform(get("/api/transactions/1")
                        .with(user(createUserDetails())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Lunch"));
    }

    @Test
    void unauthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isUnauthorized());
    }
}
