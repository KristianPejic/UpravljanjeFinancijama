package org.example.sustavzaupravljajeosobnimfinancijama.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management endpoints")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create a new transaction")
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all transactions")
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(transactionService.getAllTransactions(userId));
    }

    @Operation(summary = "Search and filter transactions with pagination")
    @GetMapping("/search")
    public ResponseEntity<Page<TransactionResponse>> searchTransactions(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(transactionService.searchTransactions(
                userId, type, categoryId, startDate, endDate, minAmount, maxAmount, keyword, pageable));
    }

    @Operation(summary = "Get transaction by ID")
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(userId, id));
    }

    @Operation(summary = "Update a transaction")
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.updateTransaction(userId, id, request));
    }

    @Operation(summary = "Delete a transaction")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {
        transactionService.deleteTransaction(userId, id);
        return ResponseEntity.noContent().build();
    }
}
