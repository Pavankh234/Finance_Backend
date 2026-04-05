package com.finance.controller;

import com.finance.domain.entity.FinancialRecord;
import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard summary and analytics")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) { this.dashboardService = dashboardService; }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardSummary()));
    }

    @GetMapping("/summary/period")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get dashboard summary for period")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummaryForPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboardSummaryForPeriod(startDate, endDate)));
    }

    @GetMapping("/totals/income")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get total income")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalIncome() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getTotalIncome()));
    }

    @GetMapping("/totals/expenses")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get total expenses")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalExpenses() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getTotalExpenses()));
    }

    @GetMapping("/totals/balance")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get net balance")
    public ResponseEntity<ApiResponse<BigDecimal>> getNetBalance() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getNetBalance()));
    }

    @GetMapping("/categories/income")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get income by category")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getIncomeByCategory() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getCategoryTotals(FinancialRecord.RecordType.INCOME)));
    }

    @GetMapping("/categories/expenses")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get expenses by category")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getExpensesByCategory() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getCategoryTotals(FinancialRecord.RecordType.EXPENSE)));
    }

    @GetMapping("/trends/monthly")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get monthly trends")
    public ResponseEntity<ApiResponse<List<DashboardSummaryResponse.MonthlyTrend>>> getMonthlyTrends(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getMonthlyTrends(startDate, endDate)));
    }
}
