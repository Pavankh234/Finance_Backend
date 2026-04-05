package com.finance.dto.request;

import com.finance.domain.entity.FinancialRecord;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RecordFilterRequest {
    private FinancialRecord.RecordType type;
    private FinancialRecord.Category category;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String searchTerm;
    private int page = 0;
    private int size = 20;
    private String sortBy = "date";
    private String sortDirection = "DESC";

    public RecordFilterRequest() {}
    public FinancialRecord.RecordType getType() { return type; }
    public void setType(FinancialRecord.RecordType type) { this.type = type; }
    public FinancialRecord.Category getCategory() { return category; }
    public void setCategory(FinancialRecord.Category category) { this.category = category; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getMinAmount() { return minAmount; }
    public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public String getSortDirection() { return sortDirection; }
    public void setSortDirection(String sortDirection) { this.sortDirection = sortDirection; }
}
