package com.finance.controller;

import com.finance.domain.entity.FinancialRecord;
import com.finance.dto.request.CreateRecordRequest;
import com.finance.dto.request.RecordFilterRequest;
import com.finance.dto.request.UpdateRecordRequest;
import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.dto.response.PageResponse;
import com.finance.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@Tag(name = "Financial Records", description = "Financial record management")
public class FinancialRecordController {
    private final FinancialRecordService recordService;

    public FinancialRecordController(FinancialRecordService recordService) { this.recordService = recordService; }

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get all records")
    public ResponseEntity<ApiResponse<PageResponse<FinancialRecordResponse>>> getAllRecords(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy, @RequestParam(defaultValue = "DESC") String sortDirection) {
        return ResponseEntity.ok(ApiResponse.success(recordService.getAllRecords(page, size, sortBy, sortDirection)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get record by ID")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(recordService.getRecordById(id)));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Filter records")
    public ResponseEntity<ApiResponse<PageResponse<FinancialRecordResponse>>> filterRecords(
            @RequestParam(required = false) FinancialRecord.RecordType type,
            @RequestParam(required = false) FinancialRecord.Category category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount, @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        RecordFilterRequest filter = new RecordFilterRequest();
        filter.setType(type); filter.setCategory(category); filter.setStartDate(startDate); filter.setEndDate(endDate);
        filter.setMinAmount(minAmount); filter.setMaxAmount(maxAmount); filter.setSearchTerm(search);
        filter.setPage(page); filter.setSize(size); filter.setSortBy(sortBy); filter.setSortDirection(sortDirection);
        return ResponseEntity.ok(ApiResponse.success(recordService.filterRecords(filter)));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    @Operation(summary = "Get recent records")
    public ResponseEntity<ApiResponse<List<FinancialRecordResponse>>> getRecentRecords(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(recordService.getRecentRecords(limit)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Create record")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> createRecord(@Valid @RequestBody CreateRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Record created successfully", recordService.createRecord(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Update record")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> updateRecord(@PathVariable Long id, @Valid @RequestBody UpdateRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Record updated successfully", recordService.updateRecord(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete record")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Record deleted successfully"));
    }

    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Restore record")
    public ResponseEntity<ApiResponse<FinancialRecordResponse>> restoreRecord(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Record restored successfully", recordService.restoreRecord(id)));
    }

    @DeleteMapping("/{id}/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Permanently delete record")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteRecord(@PathVariable Long id) {
        recordService.hardDeleteRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Record permanently deleted"));
    }
}
