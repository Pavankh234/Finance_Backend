package com.finance.repository;

import com.finance.domain.entity.FinancialRecord;
import com.finance.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long>, JpaSpecificationExecutor<FinancialRecord> {

    Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);
    Page<FinancialRecord> findByDeletedFalse(Pageable pageable);
    Page<FinancialRecord> findByCreatedByAndDeletedFalse(User user, Pageable pageable);

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.deleted = false AND fr.date BETWEEN :startDate AND :endDate")
    List<FinancialRecord> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr WHERE fr.deleted = false AND fr.type = :type")
    BigDecimal sumByType(@Param("type") FinancialRecord.RecordType type);

    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr WHERE fr.deleted = false AND fr.type = :type AND fr.date BETWEEN :startDate AND :endDate")
    BigDecimal sumByTypeAndDateRange(@Param("type") FinancialRecord.RecordType type, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT fr.category, COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr WHERE fr.deleted = false AND fr.type = :type GROUP BY fr.category")
    List<Object[]> sumByCategoryAndType(@Param("type") FinancialRecord.RecordType type);

    @Query("SELECT fr.category, COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr WHERE fr.deleted = false AND fr.type = :type AND fr.date BETWEEN :startDate AND :endDate GROUP BY fr.category")
    List<Object[]> sumByCategoryTypeAndDateRange(@Param("type") FinancialRecord.RecordType type, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT FUNCTION('YEAR', fr.date), FUNCTION('MONTH', fr.date), fr.type, COALESCE(SUM(fr.amount), 0) FROM FinancialRecord fr WHERE fr.deleted = false AND fr.date BETWEEN :startDate AND :endDate GROUP BY FUNCTION('YEAR', fr.date), FUNCTION('MONTH', fr.date), fr.type ORDER BY FUNCTION('YEAR', fr.date), FUNCTION('MONTH', fr.date)")
    List<Object[]> getMonthlyTrends(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT fr FROM FinancialRecord fr WHERE fr.deleted = false ORDER BY fr.createdAt DESC")
    List<FinancialRecord> findRecentRecords(Pageable pageable);

    @Query("SELECT COUNT(fr) FROM FinancialRecord fr WHERE fr.deleted = false")
    long countActiveRecords();

    @Query("SELECT COUNT(fr) FROM FinancialRecord fr WHERE fr.deleted = false AND fr.type = :type")
    long countByType(@Param("type") FinancialRecord.RecordType type);
}
