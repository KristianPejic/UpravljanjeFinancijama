package org.example.sustavzaupravljajeosobnimfinancijama.config;

import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.model.Category;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.model.User;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.CategoryRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@example.com");
            user.setPassword(passwordEncoder.encode("password123"));
            user = userRepository.save(user);

            createCategory("Food", TransactionType.EXPENSE, user);
            createCategory("Transport", TransactionType.EXPENSE, user);
            createCategory("Utilities", TransactionType.EXPENSE, user);
            createCategory("Entertainment", TransactionType.EXPENSE, user);
            createCategory("Salary", TransactionType.INCOME, user);
            createCategory("Other", TransactionType.INCOME, user);
        }
    }

    private void createCategory(String name, TransactionType type, User user) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setUser(user);
        categoryRepository.save(category);
    }
}
