package org.example.sustavzaupravljajeosobnimfinancijama.repository;

import org.example.sustavzaupravljajeosobnimfinancijama.model.Category;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);

    List<Category> findByUserIdAndType(Long userId, TransactionType type);
}
