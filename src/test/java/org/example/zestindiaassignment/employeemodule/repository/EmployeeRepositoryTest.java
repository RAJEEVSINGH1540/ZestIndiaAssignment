package org.example.zestindiaassignment.employeemodule.repository;

import org.example.zestindiaassignment.employeemodule.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Employee Repository Tests")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();

        employeeRepository.save(Employee.builder()
                .empId("EMP2025-0001")
                .name("Jane Smith")
                .email("jane@example.com")
                .department("HR")
                .position("Manager")
                .salary(new BigDecimal("65000.00"))
                .dateOfJoining(LocalDate.of(2023, 6, 1))
                .status(Employee.EmployeeStatus.ACTIVE)
                .build());

        employeeRepository.save(Employee.builder()
                .empId("EMP2025-0002")
                .name("Bob Johnson")
                .email("bob@example.com")
                .department("Engineering")
                .position("Developer")
                .salary(new BigDecimal("80000.00"))
                .dateOfJoining(LocalDate.of(2022, 3, 15))
                .status(Employee.EmployeeStatus.ACTIVE)
                .build());
    }

    @Test
    @DisplayName("findByEmail - should return employee when email exists")
    void findByEmail_Found() {
        Optional<Employee> result = employeeRepository.findByEmail("jane@example.com");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("findByEmail - should return empty when email not found")
    void findByEmail_NotFound() {
        Optional<Employee> result = employeeRepository.findByEmail("nobody@example.com");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail - should return true when email exists")
    void existsByEmail_True() {
        assertThat(employeeRepository.existsByEmail("jane@example.com")).isTrue();
    }

    @Test
    @DisplayName("existsByEmail - should return false when email not found")
    void existsByEmail_False() {
        assertThat(employeeRepository.existsByEmail("nobody@example.com")).isFalse();
    }

    @Test
    @DisplayName("findByDepartment - should return paginated results for department")
    void findByDepartment_Paginated() {
        Page<Employee> result = employeeRepository
                .findByDepartment("HR", PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDepartment()).isEqualTo("HR");
    }

    @Test
    @DisplayName("findByStatus - should return paginated results for ACTIVE status")
    void findByStatus_Active() {
        Page<Employee> result = employeeRepository
                .findByStatus(Employee.EmployeeStatus.ACTIVE, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("searchEmployees - should filter by name")
    void search_ByName() {
        Page<Employee> result = employeeRepository
                .searchEmployees("Jane", null, null, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("searchEmployees - should filter by department")
    void search_ByDepartment() {
        Page<Employee> result = employeeRepository
                .searchEmployees(null, "Engineering", null, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDepartment()).isEqualTo("Engineering");
    }

    @Test
    @DisplayName("searchEmployees - should return all when no filters")
    void search_NoFilters() {
        Page<Employee> result = employeeRepository
                .searchEmployees(null, null, null, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("searchEmployees - should return empty when no match")
    void search_NoMatch() {
        Page<Employee> result = employeeRepository
                .searchEmployees("XYZ_NOT_EXIST", null, null, PageRequest.of(0, 10));
        assertThat(result.getContent()).isEmpty();
    }
}