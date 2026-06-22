package org.example.sustavzaupravljajeosobnimfinancijama.service;

import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createTransaction(Long userId, TransactionRequest request);

    List<TransactionResponse> getAllTransactions(Long userId);

    TransactionResponse getTransactionById(Long userId, Long transactionId);

    TransactionResponse updateTransaction(Long userId, Long transactionId, TransactionRequest request);

    void deleteTransaction(Long userId, Long transactionId);
}
