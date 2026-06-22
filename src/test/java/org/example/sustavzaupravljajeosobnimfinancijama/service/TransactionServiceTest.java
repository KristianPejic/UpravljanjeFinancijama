package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.exception.ResourceNotFoundException;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Category;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Transaction;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.model.User;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.CategoryRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.TransactionRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.UserRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;
    private Category category;
    private Transaction transaction;

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

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("50.00"));
        transaction.setDescription("Lunch");
        transaction.setDate(LocalDate.of(2026, 6, 15));
        transaction.setType(TransactionType.EXPENSE);
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createTransaction_ShouldReturnTransactionResponse() {
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("50.00"), "Lunch", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponse response = transactionService.createTransaction(1L, request);

        assertNotNull(response);
        assertEquals(new BigDecimal("50.00"), response.getAmount());
        assertEquals("Lunch", response.getDescription());
        assertEquals("Food", response.getCategoryName());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void createTransaction_UserNotFound_ShouldThrowException() {
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("50.00"), "Lunch", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(1L, request));
    }

    @Test
    void createTransaction_CategoryNotFound_ShouldThrowException() {
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("50.00"), "Lunch", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, 99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.createTransaction(1L, request));
    }

    @Test
    void getAllTransactions_ShouldReturnList() {
        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(new BigDecimal("3000.00"));
        transaction2.setDescription("Monthly salary");
        transaction2.setDate(LocalDate.of(2026, 6, 1));
        transaction2.setType(TransactionType.INCOME);
        transaction2.setCategory(category);
        transaction2.setUser(user);
        transaction2.setCreatedAt(LocalDateTime.now());

        when(transactionRepository.findByUserId(1L)).thenReturn(Arrays.asList(transaction, transaction2));

        List<TransactionResponse> responses = transactionService.getAllTransactions(1L);

        assertEquals(2, responses.size());
    }

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransactionById(1L, 1L);

        assertNotNull(response);
        assertEquals(new BigDecimal("50.00"), response.getAmount());
    }

    @Test
    void getTransactionById_NotFound_ShouldThrowException() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionById(1L, 1L));
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() {
        TransactionRequest request = new TransactionRequest(
                new BigDecimal("75.00"), "Dinner", LocalDate.of(2026, 6, 15),
                TransactionType.EXPENSE, 1L);

        Transaction updated = new Transaction();
        updated.setId(1L);
        updated.setAmount(new BigDecimal("75.00"));
        updated.setDescription("Dinner");
        updated.setDate(LocalDate.of(2026, 6, 15));
        updated.setType(TransactionType.EXPENSE);
        updated.setCategory(category);
        updated.setUser(user);
        updated.setCreatedAt(LocalDateTime.now());

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updated);

        TransactionResponse response = transactionService.updateTransaction(1L, 1L, request);

        assertEquals(new BigDecimal("75.00"), response.getAmount());
        assertEquals("Dinner", response.getDescription());
    }

    @Test
    void deleteTransaction_ShouldCallDelete() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(1L, 1L);

        verify(transactionRepository).delete(transaction);
    }
}
