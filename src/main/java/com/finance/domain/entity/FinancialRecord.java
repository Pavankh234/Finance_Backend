package com.finance.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecordType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Category category;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String description;

    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    private boolean deleted = false;
    private LocalDateTime deletedAt;

    public enum RecordType { INCOME, EXPENSE }

    public enum Category {
        SALARY, INVESTMENT, FREELANCE, BONUS, REFUND, OTHER_INCOME,
        FOOD, TRANSPORTATION, UTILITIES, ENTERTAINMENT, HEALTHCARE,
        EDUCATION, SHOPPING, RENT, INSURANCE, TAXES, OTHER_EXPENSE
    }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public void softDelete() { this.deleted = true; this.deletedAt = LocalDateTime.now(); }
    public void restore() { this.deleted = false; this.deletedAt = null; }
}
