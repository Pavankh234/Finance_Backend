package com.finance.config;

import com.finance.domain.entity.FinancialRecord;
import com.finance.domain.entity.Role;
import com.finance.domain.entity.User;
import com.finance.repository.FinancialRecordRepository;
import com.finance.repository.RoleRepository;
import com.finance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FinancialRecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, FinancialRecordRepository recordRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initializeRoles();
        initializeUsers();
        initializeSampleRecords();
        log.info("Data initialization completed successfully");
    }

    private void initializeRoles() {
        if (roleRepository.count() > 0) return;

        Role viewerRole = Role.builder().name(Role.RoleName.VIEWER).description("Can only view dashboard data")
            .permissions(Set.of(Role.Permission.RECORD_READ, Role.Permission.DASHBOARD_VIEW)).build();
        roleRepository.save(viewerRole);

        Role analystRole = Role.builder().name(Role.RoleName.ANALYST).description("Can view records and access insights")
            .permissions(Set.of(Role.Permission.RECORD_READ, Role.Permission.RECORD_CREATE, Role.Permission.RECORD_UPDATE,
                Role.Permission.DASHBOARD_VIEW, Role.Permission.DASHBOARD_ANALYTICS, Role.Permission.USER_READ)).build();
        roleRepository.save(analystRole);

        Role adminRole = Role.builder().name(Role.RoleName.ADMIN).description("Full management access")
            .permissions(Set.of(Role.Permission.USER_READ, Role.Permission.USER_CREATE, Role.Permission.USER_UPDATE, Role.Permission.USER_DELETE,
                Role.Permission.RECORD_READ, Role.Permission.RECORD_CREATE, Role.Permission.RECORD_UPDATE, Role.Permission.RECORD_DELETE,
                Role.Permission.DASHBOARD_VIEW, Role.Permission.DASHBOARD_ANALYTICS, Role.Permission.ROLE_MANAGE)).build();
        roleRepository.save(adminRole);

        log.info("Roles initialized: VIEWER, ANALYST, ADMIN");
    }

    private void initializeUsers() {
        if (userRepository.count() > 0) return;

        Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN).orElseThrow();
        Role analystRole = roleRepository.findByName(Role.RoleName.ANALYST).orElseThrow();
        Role viewerRole = roleRepository.findByName(Role.RoleName.VIEWER).orElseThrow();

        userRepository.save(User.builder().username("admin").email("admin@finance.com").password(passwordEncoder.encode("admin123")).fullName("System Administrator").status(User.UserStatus.ACTIVE).roles(Set.of(adminRole)).build());
        userRepository.save(User.builder().username("analyst").email("analyst@finance.com").password(passwordEncoder.encode("analyst123")).fullName("Financial Analyst").status(User.UserStatus.ACTIVE).roles(Set.of(analystRole)).build());
        userRepository.save(User.builder().username("viewer").email("viewer@finance.com").password(passwordEncoder.encode("viewer123")).fullName("Dashboard Viewer").status(User.UserStatus.ACTIVE).roles(Set.of(viewerRole)).build());

        log.info("Default users created: admin, analyst, viewer");
    }

    private void initializeSampleRecords() {
        if (recordRepository.count() > 0) return;

        User admin = userRepository.findByUsername("admin").orElseThrow();
        LocalDate today = LocalDate.now();

        // Income records
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("5000.00")).type(FinancialRecord.RecordType.INCOME).category(FinancialRecord.Category.SALARY).date(today.minusDays(30)).description("Monthly salary").notes("January salary").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("5000.00")).type(FinancialRecord.RecordType.INCOME).category(FinancialRecord.Category.SALARY).date(today).description("Monthly salary").notes("February salary").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("500.00")).type(FinancialRecord.RecordType.INCOME).category(FinancialRecord.Category.FREELANCE).date(today.minusDays(15)).description("Freelance project").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("200.00")).type(FinancialRecord.RecordType.INCOME).category(FinancialRecord.Category.INVESTMENT).date(today.minusDays(10)).description("Dividend payment").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("1000.00")).type(FinancialRecord.RecordType.INCOME).category(FinancialRecord.Category.BONUS).date(today.minusDays(5)).description("Performance bonus").createdBy(admin).build());

        // Expense records
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("1500.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.RENT).date(today.minusDays(28)).description("Monthly rent").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("300.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.UTILITIES).date(today.minusDays(25)).description("Utility bills").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("400.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.FOOD).date(today.minusDays(20)).description("Groceries").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("150.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.TRANSPORTATION).date(today.minusDays(18)).description("Fuel").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("100.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.ENTERTAINMENT).date(today.minusDays(12)).description("Movie tickets").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("250.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.HEALTHCARE).date(today.minusDays(8)).description("Doctor visit").createdBy(admin).build());
        recordRepository.save(FinancialRecord.builder().amount(new BigDecimal("500.00")).type(FinancialRecord.RecordType.EXPENSE).category(FinancialRecord.Category.SHOPPING).date(today.minusDays(3)).description("Clothing").createdBy(admin).build());

        log.info("Sample financial records created");
    }
}
