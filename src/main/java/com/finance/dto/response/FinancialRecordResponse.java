package com.finance.dto.response;

import com.finance.domain.entity.FinancialRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecordResponse {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String category;
    private LocalDate date;
    private String description;
    private String notes;
    private String createdByUsername;
    private Long createdById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FinancialRecordResponse fromEntity(FinancialRecord record) {
        return FinancialRecordResponse.builder()
            .id(record.getId())
            .amount(record.getAmount())
            .type(record.getType().name())
            .category(record.getCategory().name())
            .date(record.getDate())
            .description(record.getDescription())
            .notes(record.getNotes())
            .createdByUsername(record.getCreatedBy().getUsername())
            .createdById(record.getCreatedBy().getId())
            .createdAt(record.getCreatedAt())
            .updatedAt(record.getUpdatedAt())
            .build();
    }
}
