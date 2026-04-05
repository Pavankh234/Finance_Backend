package com.finance.service;

import com.finance.domain.entity.FinancialRecord;
import com.finance.domain.entity.User;
import com.finance.dto.request.CreateRecordRequest;
import com.finance.dto.request.RecordFilterRequest;
import com.finance.dto.request.UpdateRecordRequest;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.dto.response.PageResponse;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.FinancialRecordRepository;
import com.finance.security.SecurityUtils;
import com.finance.service.specification.FinancialRecordSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialRecordService {
    private static final Logger log = LoggerFactory.getLogger(FinancialRecordService.class);
    private final FinancialRecordRepository recordRepository;

    public FinancialRecordService(FinancialRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<FinancialRecordResponse> getAllRecords(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FinancialRecord> recordPage = recordRepository.findByDeletedFalse(pageable);
        return PageResponse.from(recordPage, FinancialRecordResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public FinancialRecordResponse getRecordById(Long id) {
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Financial Record", "id", id));
        return FinancialRecordResponse.fromEntity(record);
    }

    @Transactional(readOnly = true)
    public PageResponse<FinancialRecordResponse> filterRecords(RecordFilterRequest filter) {
        Sort sort = filter.getSortDirection().equalsIgnoreCase("DESC") ? Sort.by(filter.getSortBy()).descending() : Sort.by(filter.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        Specification<FinancialRecord> spec = FinancialRecordSpecification.buildSpecification(filter);
        Page<FinancialRecord> recordPage = recordRepository.findAll(spec, pageable);
        return PageResponse.from(recordPage, FinancialRecordResponse::fromEntity);
    }

    @Transactional
    public FinancialRecordResponse createRecord(CreateRecordRequest request) {
        User currentUser = SecurityUtils.getCurrentUserOrThrow();
        FinancialRecord record = FinancialRecord.builder().amount(request.getAmount()).type(request.getType()).category(request.getCategory()).date(request.getDate()).description(request.getDescription()).notes(request.getNotes()).createdBy(currentUser).build();
        record = recordRepository.save(record);
        log.info("Created financial record {} by user {}", record.getId(), currentUser.getUsername());
        return FinancialRecordResponse.fromEntity(record);
    }

    @Transactional
    public FinancialRecordResponse updateRecord(Long id, UpdateRecordRequest request) {
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Financial Record", "id", id));
        if (request.getAmount() != null) record.setAmount(request.getAmount());
        if (request.getType() != null) record.setType(request.getType());
        if (request.getCategory() != null) record.setCategory(request.getCategory());
        if (request.getDate() != null) record.setDate(request.getDate());
        if (request.getDescription() != null) record.setDescription(request.getDescription());
        if (request.getNotes() != null) record.setNotes(request.getNotes());
        record = recordRepository.save(record);
        log.info("Updated financial record {}", record.getId());
        return FinancialRecordResponse.fromEntity(record);
    }

    @Transactional
    public void deleteRecord(Long id) {
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Financial Record", "id", id));
        record.softDelete();
        recordRepository.save(record);
        log.info("Soft deleted financial record {}", id);
    }

    @Transactional
    public void hardDeleteRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Financial Record", "id", id));
        recordRepository.delete(record);
        log.info("Hard deleted financial record {}", id);
    }

    @Transactional
    public FinancialRecordResponse restoreRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Financial Record", "id", id));
        record.restore();
        record = recordRepository.save(record);
        log.info("Restored financial record {}", id);
        return FinancialRecordResponse.fromEntity(record);
    }

    @Transactional(readOnly = true)
    public List<FinancialRecordResponse> getRecentRecords(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return recordRepository.findRecentRecords(pageable).stream().map(FinancialRecordResponse::fromEntity).collect(Collectors.toList());
    }
}
