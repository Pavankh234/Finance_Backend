package com.finance.dto.request;

import com.finance.domain.entity.FinancialRecord;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
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
}
