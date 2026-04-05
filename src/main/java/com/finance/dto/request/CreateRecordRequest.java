package com.finance.dto.request;

import com.finance.domain.entity.FinancialRecord;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateRecordRequest {
    @NotNull @DecimalMin("0.01") @Digits(integer = 15, fraction = 4)
    private BigDecimal amount;

    @NotNull
    private FinancialRecord.RecordType type;

    @NotNull
    private FinancialRecord.Category category;

    @NotNull @PastOrPresent
    private LocalDate date;

    @Size(max = 500)
    private String description;

    @Size(max = 1000)
    private String notes;
}
