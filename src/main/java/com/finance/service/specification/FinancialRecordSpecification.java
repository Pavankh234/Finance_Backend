package com.finance.service.specification;

import com.finance.domain.entity.FinancialRecord;
import com.finance.dto.request.RecordFilterRequest;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class FinancialRecordSpecification {
    private FinancialRecordSpecification() {}

    public static Specification<FinancialRecord> buildSpecification(RecordFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
            if (filter.getType() != null) predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            if (filter.getCategory() != null) predicates.add(criteriaBuilder.equal(root.get("category"), filter.getCategory()));
            if (filter.getStartDate() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            if (filter.getEndDate() != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            if (filter.getMinAmount() != null) predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
            if (filter.getMaxAmount() != null) predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
            if (filter.getSearchTerm() != null && !filter.getSearchTerm().isBlank()) {
                String searchPattern = "%" + filter.getSearchTerm().toLowerCase() + "%";
                Predicate descPred = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);
                Predicate notesPred = criteriaBuilder.like(criteriaBuilder.lower(root.get("notes")), searchPattern);
                predicates.add(criteriaBuilder.or(descPred, notesPred));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
