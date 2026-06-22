package org.example.sustavzaupravljajeosobnimfinancijama.repository;

import org.example.sustavzaupravljajeosobnimfinancijama.model.Transaction;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionSpecification {

    public static Specification<Transaction> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Transaction> dateAfter(LocalDate startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), startDate);
    }

    public static Specification<Transaction> dateBefore(LocalDate endDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), endDate);
    }

    public static Specification<Transaction> amountGreaterThan(BigDecimal minAmount) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<Transaction> amountLessThan(BigDecimal maxAmount) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), maxAmount);
    }

    public static Specification<Transaction> descriptionContains(String keyword) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
    }
}
