package org.example.sustavzaupravljajeosobnimfinancijama.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearlyReportResponse {

    private int year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private List<MonthlySummary> monthlySummaries;
    private List<CategorySummary> expensesByCategory;
    private List<CategorySummary> incomeByCategory;
}
