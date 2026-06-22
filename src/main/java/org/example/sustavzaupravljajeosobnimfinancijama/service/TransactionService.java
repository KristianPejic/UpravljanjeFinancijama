package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(Long userId, TransactionRequest request);

    List<TransactionResponse> getAllTransactions(Long userId);

    TransactionResponse getTransactionById(Long userId, Long transactionId);

    TransactionResponse updateTransaction(Long userId, Long transactionId, TransactionRequest request);

    void deleteTransaction(Long userId, Long transactionId);

    Page<TransactionResponse> searchTransactions(Long userId, TransactionType type, Long categoryId,
                                                  LocalDate startDate, LocalDate endDate,
                                                  BigDecimal minAmount, BigDecimal maxAmount,
                                                  String keyword, Pageable pageable);
}
