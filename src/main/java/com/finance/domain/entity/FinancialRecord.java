package com.finance.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
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
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean deleted = false;
    private LocalDateTime deletedAt;

    public enum RecordType { INCOME, EXPENSE }

    public enum Category {
        SALARY, INVESTMENT, FREELANCE, BONUS, REFUND, OTHER_INCOME,
        FOOD, TRANSPORTATION, UTILITIES, ENTERTAINMENT, HEALTHCARE,
        EDUCATION, SHOPPING, RENT, INSURANCE, TAXES, OTHER_EXPENSE
    }

    public FinancialRecord() {}

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public void softDelete() { this.deleted = true; this.deletedAt = LocalDateTime.now(); }
    public void restore() { this.deleted = false; this.deletedAt = null; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public RecordType getType() { return type; }
    public void setType(RecordType type) { this.type = type; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public static FinancialRecordBuilder builder() { return new FinancialRecordBuilder(); }

    public static class FinancialRecordBuilder {
        private Long id;
        private BigDecimal amount;
        private RecordType type;
        private Category category;
        private LocalDate date;
        private String description;
        private String notes;
        private User createdBy;

        public FinancialRecordBuilder id(Long id) { this.id = id; return this; }
        public FinancialRecordBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public FinancialRecordBuilder type(RecordType type) { this.type = type; return this; }
        public FinancialRecordBuilder category(Category category) { this.category = category; return this; }
        public FinancialRecordBuilder date(LocalDate date) { this.date = date; return this; }
        public FinancialRecordBuilder description(String description) { this.description = description; return this; }
        public FinancialRecordBuilder notes(String notes) { this.notes = notes; return this; }
        public FinancialRecordBuilder createdBy(User createdBy) { this.createdBy = createdBy; return this; }

        public FinancialRecord build() {
            FinancialRecord r = new FinancialRecord();
            r.id = id; r.amount = amount; r.type = type; r.category = category;
            r.date = date; r.description = description; r.notes = notes; r.createdBy = createdBy;
            return r;
        }
    }
}
