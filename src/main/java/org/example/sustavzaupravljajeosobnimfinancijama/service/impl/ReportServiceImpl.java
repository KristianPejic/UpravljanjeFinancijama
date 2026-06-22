package org.example.sustavzaupravljajeosobnimfinancijama.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.*;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.TransactionRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository transactionRepository;

    @Override
    public MonthlyReportResponse getMonthlyReport(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        BigDecimal totalIncome = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, TransactionType.INCOME, startDate, endDate);
        BigDecimal totalExpense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, TransactionType.EXPENSE, startDate, endDate);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<CategorySummary> expensesByCategory = buildCategorySummaries(
                userId, TransactionType.EXPENSE, startDate, endDate, totalExpense);
        List<CategorySummary> incomeByCategory = buildCategorySummaries(
                userId, TransactionType.INCOME, startDate, endDate, totalIncome);

        String monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return new MonthlyReportResponse(year, month, monthName,
                totalIncome, totalExpense, balance, expensesByCategory, incomeByCategory);
    }

    @Override
    public YearlyReportResponse getYearlyReport(Long userId, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        BigDecimal totalIncome = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, TransactionType.INCOME, startDate, endDate);
        BigDecimal totalExpense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                userId, TransactionType.EXPENSE, startDate, endDate);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<MonthlySummary> monthlySummaries = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate monthStart = yearMonth.atDay(1);
            LocalDate monthEnd = yearMonth.atEndOfMonth();

            BigDecimal monthIncome = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                    userId, TransactionType.INCOME, monthStart, monthEnd);
            BigDecimal monthExpense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                    userId, TransactionType.EXPENSE, monthStart, monthEnd);

            String monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlySummaries.add(new MonthlySummary(month, monthName,
                    monthIncome, monthExpense, monthIncome.subtract(monthExpense)));
        }

        List<CategorySummary> expensesByCategory = buildCategorySummaries(
                userId, TransactionType.EXPENSE, startDate, endDate, totalExpense);
        List<CategorySummary> incomeByCategory = buildCategorySummaries(
                userId, TransactionType.INCOME, startDate, endDate, totalIncome);

        return new YearlyReportResponse(year, totalIncome, totalExpense, balance,
                monthlySummaries, expensesByCategory, incomeByCategory);
    }

    private List<CategorySummary> buildCategorySummaries(Long userId, TransactionType type,
                                                          LocalDate startDate, LocalDate endDate,
                                                          BigDecimal total) {
        List<Object[]> results = transactionRepository.sumByCategoryAndUserIdAndTypeAndDateBetween(
                userId, type, startDate, endDate);

        List<CategorySummary> summaries = new ArrayList<>();
        for (Object[] row : results) {
            String categoryName = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            double percentage = total.compareTo(BigDecimal.ZERO) > 0
                    ? amount.divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                    : 0.0;
            summaries.add(new CategorySummary(categoryName, amount, percentage));
        }
        return summaries;
    }
}
