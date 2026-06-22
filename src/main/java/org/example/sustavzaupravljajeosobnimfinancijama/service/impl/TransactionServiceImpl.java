package org.example.sustavzaupravljajeosobnimfinancijama.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionRequest;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.TransactionResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.exception.ResourceNotFoundException;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Category;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Transaction;
import org.example.sustavzaupravljajeosobnimfinancijama.model.User;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.CategoryRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.TransactionRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.UserRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public TransactionResponse createTransaction(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setCategory(category);
        transaction.setUser(user);

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    @Override
    public List<TransactionResponse> getAllTransactions(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionById(Long userId, Long transactionId) {
        Transaction transaction = findTransactionByIdAndUserId(transactionId, userId);
        return mapToResponse(transaction);
    }

    @Override
    public TransactionResponse updateTransaction(Long userId, Long transactionId, TransactionRequest request) {
        Transaction transaction = findTransactionByIdAndUserId(transactionId, userId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setCategory(category);

        Transaction updated = transactionRepository.save(transaction);
        return mapToResponse(updated);
    }

    @Override
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = findTransactionByIdAndUserId(transactionId, userId);
        transactionRepository.delete(transaction);
    }

    private Transaction findTransactionByIdAndUserId(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + transactionId));
        if (!transaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transaction not found with id: " + transactionId);
        }
        return transaction;
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCategory().getName(),
                transaction.getCategory().getId(),
                transaction.getCreatedAt()
        );
    }
}
