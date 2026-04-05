package com.finance.dto.request;

import com.finance.domain.entity.FinancialRecord;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateRecordRequest {
    @DecimalMin("0.01") @Digits(integer = 15, fraction = 4)
    private BigDecimal amount;
    private FinancialRecord.RecordType type;
    private FinancialRecord.Category category;
    @PastOrPresent
    private LocalDate date;
    @Size(max = 500)
    private String description;
    @Size(max = 1000)
    private String notes;

    public UpdateRecordRequest() {}
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public FinancialRecord.RecordType getType() { return type; }
    public void setType(FinancialRecord.RecordType type) { this.type = type; }
    public FinancialRecord.Category getCategory() { return category; }
    public void setCategory(FinancialRecord.Category category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
