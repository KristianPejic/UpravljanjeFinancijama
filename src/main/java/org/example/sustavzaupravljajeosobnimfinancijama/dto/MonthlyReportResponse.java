package org.example.sustavzaupravljajeosobnimfinancijama.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportResponse {

    private int year;
    private int month;
    private String monthName;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private List<CategorySummary> expensesByCategory;
    private List<CategorySummary> incomeByCategory;
}
