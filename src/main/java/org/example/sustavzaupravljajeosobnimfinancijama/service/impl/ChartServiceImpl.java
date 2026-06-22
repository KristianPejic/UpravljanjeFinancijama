package org.example.sustavzaupravljajeosobnimfinancijama.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.ChartDataResponse;
import org.example.sustavzaupravljajeosobnimfinancijama.dto.DataSet;
import org.example.sustavzaupravljajeosobnimfinancijama.model.TransactionType;
import org.example.sustavzaupravljajeosobnimfinancijama.repository.TransactionRepository;
import org.example.sustavzaupravljajeosobnimfinancijama.service.ChartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final TransactionRepository transactionRepository;

    @Override
    public ChartDataResponse getSpendingByCategory(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Object[]> results = transactionRepository.sumByCategoryAndUserIdAndTypeAndDateBetween(
                userId, TransactionType.EXPENSE, startDate, endDate);

        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        for (Object[] row : results) {
            labels.add((String) row[0]);
            data.add((BigDecimal) row[1]);
        }

        DataSet dataSet = new DataSet("Spending by Category", data);
        return new ChartDataResponse(labels, Collections.singletonList(dataSet));
    }

    @Override
    public ChartDataResponse getMonthlyTrend(Long userId, int year) {
        List<String> labels = new ArrayList<>();
        List<BigDecimal> incomeData = new ArrayList<>();
        List<BigDecimal> expenseData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            labels.add(monthName);

            BigDecimal income = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                    userId, TransactionType.INCOME, startDate, endDate);
            BigDecimal expense = transactionRepository.sumByUserIdAndTypeAndDateBetween(
                    userId, TransactionType.EXPENSE, startDate, endDate);

            incomeData.add(income);
            expenseData.add(expense);
        }

        List<DataSet> datasets = new ArrayList<>();
        datasets.add(new DataSet("Income", incomeData));
        datasets.add(new DataSet("Expense", expenseData));

        return new ChartDataResponse(labels, datasets);
    }
}
