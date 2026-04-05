package com.finance.service;

import com.finance.domain.entity.FinancialRecord;
import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final FinancialRecordRepository recordRepository;

    public DashboardService(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummary() {
        BigDecimal totalIncome = recordRepository.sumByType(FinancialRecord.RecordType.INCOME);
        BigDecimal totalExpenses = recordRepository.sumByType(FinancialRecord.RecordType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);
        long totalRecords = recordRepository.countActiveRecords();
        long incomeCount = recordRepository.countByType(FinancialRecord.RecordType.INCOME);
        long expenseCount = recordRepository.countByType(FinancialRecord.RecordType.EXPENSE);
        Map<String, BigDecimal> incomeByCategory = getCategoryTotals(FinancialRecord.RecordType.INCOME);
        Map<String, BigDecimal> expenseByCategory = getCategoryTotals(FinancialRecord.RecordType.EXPENSE);
        List<FinancialRecordResponse> recentActivity = recordRepository.findRecentRecords(PageRequest.of(0, 10)).stream().map(FinancialRecordResponse::fromEntity).collect(Collectors.toList());
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(11).withDayOfMonth(1);
        List<DashboardSummaryResponse.MonthlyTrend> monthlyTrends = getMonthlyTrends(startDate, endDate);

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetBalance(netBalance);
        response.setTotalRecords(totalRecords);
        response.setIncomeCount(incomeCount);
        response.setExpenseCount(expenseCount);
        response.setIncomeByCategory(incomeByCategory);
        response.setExpenseByCategory(expenseByCategory);
        response.setRecentActivity(recentActivity);
        response.setMonthlyTrends(monthlyTrends);
        return response;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummaryForPeriod(LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = recordRepository.sumByTypeAndDateRange(FinancialRecord.RecordType.INCOME, startDate, endDate);
        BigDecimal totalExpenses = recordRepository.sumByTypeAndDateRange(FinancialRecord.RecordType.EXPENSE, startDate, endDate);
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);
        Map<String, BigDecimal> incomeByCategory = getCategoryTotalsForPeriod(FinancialRecord.RecordType.INCOME, startDate, endDate);
        Map<String, BigDecimal> expenseByCategory = getCategoryTotalsForPeriod(FinancialRecord.RecordType.EXPENSE, startDate, endDate);
        List<DashboardSummaryResponse.MonthlyTrend> monthlyTrends = getMonthlyTrends(startDate, endDate);

        DashboardSummaryResponse response = new DashboardSummaryResponse();
        response.setTotalIncome(totalIncome);
        response.setTotalExpenses(totalExpenses);
        response.setNetBalance(netBalance);
        response.setIncomeByCategory(incomeByCategory);
        response.setExpenseByCategory(expenseByCategory);
        response.setMonthlyTrends(monthlyTrends);
        return response;
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getCategoryTotals(FinancialRecord.RecordType type) {
        List<Object[]> results = recordRepository.sumByCategoryAndType(type);
        Map<String, BigDecimal> categoryTotals = new LinkedHashMap<>();
        for (Object[] result : results) {
            FinancialRecord.Category category = (FinancialRecord.Category) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            categoryTotals.put(category.name(), amount);
        }
        return categoryTotals;
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getCategoryTotalsForPeriod(FinancialRecord.RecordType type, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = recordRepository.sumByCategoryTypeAndDateRange(type, startDate, endDate);
        Map<String, BigDecimal> categoryTotals = new LinkedHashMap<>();
        for (Object[] result : results) {
            FinancialRecord.Category category = (FinancialRecord.Category) result[0];
            BigDecimal amount = (BigDecimal) result[1];
            categoryTotals.put(category.name(), amount);
        }
        return categoryTotals;
    }

    @Transactional(readOnly = true)
    public List<DashboardSummaryResponse.MonthlyTrend> getMonthlyTrends(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = recordRepository.getMonthlyTrends(startDate, endDate);
        Map<String, DashboardSummaryResponse.MonthlyTrend> trendMap = new LinkedHashMap<>();
        for (Object[] result : results) {
            int year = ((Number) result[0]).intValue();
            int month = ((Number) result[1]).intValue();
            FinancialRecord.RecordType type = (FinancialRecord.RecordType) result[2];
            BigDecimal amount = (BigDecimal) result[3];
            String key = year + "-" + month;
            DashboardSummaryResponse.MonthlyTrend trend = trendMap.computeIfAbsent(key, k -> {
                DashboardSummaryResponse.MonthlyTrend t = new DashboardSummaryResponse.MonthlyTrend();
                t.setYear(year);
                t.setMonth(month);
                t.setMonthName(Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
                t.setIncome(BigDecimal.ZERO);
                t.setExpense(BigDecimal.ZERO);
                t.setNetBalance(BigDecimal.ZERO);
                return t;
            });
            if (type == FinancialRecord.RecordType.INCOME) trend.setIncome(amount);
            else trend.setExpense(amount);
            trend.setNetBalance(trend.getIncome().subtract(trend.getExpense()));
        }
        return new ArrayList<>(trendMap.values());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome() { return recordRepository.sumByType(FinancialRecord.RecordType.INCOME); }

    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses() { return recordRepository.sumByType(FinancialRecord.RecordType.EXPENSE); }

    @Transactional(readOnly = true)
    public BigDecimal getNetBalance() { return getTotalIncome().subtract(getTotalExpenses()); }
}
