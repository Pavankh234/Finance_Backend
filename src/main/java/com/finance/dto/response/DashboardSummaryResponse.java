package com.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private Long totalRecords;
    private Long incomeCount;
    private Long expenseCount;
    private Map<String, BigDecimal> incomeByCategory;
    private Map<String, BigDecimal> expenseByCategory;
    private List<FinancialRecordResponse> recentActivity;
    private List<MonthlyTrend> monthlyTrends;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrend {
        private int year;
        private int month;
        private String monthName;
        private BigDecimal income;
        private BigDecimal expense;
        private BigDecimal netBalance;

        public MonthlyTrend(int year, int month, String monthName, BigDecimal income, BigDecimal expense) {
            this.year = year; this.month = month; this.monthName = monthName;
            this.income = income; this.expense = expense;
            this.netBalance = income.subtract(expense);
        }
    }
}
