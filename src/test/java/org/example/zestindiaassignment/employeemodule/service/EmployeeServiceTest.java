package org.example.zestindiaassignment.employeemodule.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeRequestDto;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeResponseDto;
import org.example.zestindiaassignment.employeemodule.entity.Employee;
import org.example.zestindiaassignment.employeemodule.repository.EmployeeRepository;
import org.example.zestindiaassignment.employeemodule.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service Unit Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee      testEmployee;
    private EmployeeRequestDto testRequest;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder()
                .empId("EMP2025-0042")
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .position("Developer")
                .salary(new BigDecimal("75000.00"))
                .dateOfJoining(LocalDate.of(2024, 1, 15))
                .status(Employee.EmployeeStatus.ACTIVE)
                .build();

        testRequest = EmployeeRequestDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .position("Developer")
                .salary(new BigDecimal("75000.00"))
                .dateOfJoining(LocalDate.of(2024, 1, 15))
                .build();
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("getEmployeeById - should return employee when ID exists")
    void getById_Success() {
        when(employeeRepository.findById("EMP2025-0042"))
                .thenReturn(Optional.of(testEmployee));

        EmployeeResponseDto result = employeeService.getEmployeeById("EMP2025-0042");

        assertThat(result).isNotNull();
        assertThat(result.getEmpId()).isEqualTo("EMP2025-0042");
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getDepartment()).isEqualTo("Engineering");
        verify(employeeRepository, times(1)).findById("EMP2025-0042");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("getEmployeeById - should throw EntityNotFoundException when ID not found")
    void getById_NotFound() {
        when(employeeRepository.findById("INVALID"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById("INVALID"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Employee not found with ID: INVALID");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("getAllEmployees - should return paginated list")
    void getAll_Paginated() {
        Pageable pageable  = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(testEmployee));

        when(employeeRepository.findAll(pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = employeeService.getAllEmployees(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("john@example.com");
        verify(employeeRepository, times(1)).findAll(pageable);
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("createEmployee - should throw IllegalArgumentException on duplicate email")
    void create_DuplicateEmail() {
        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(testRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("updateEmployee - should update and return DTO on success")
    void update_Success() {
        when(employeeRepository.findById("EMP2025-0042"))
                .thenReturn(Optional.of(testEmployee));
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        EmployeeResponseDto result =
                employeeService.updateEmployee("EMP2025-0042", testRequest);

        assertThat(result).isNotNull();
        assertThat(result.getEmpId()).isEqualTo("EMP2025-0042");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("updateEmployee - should throw EntityNotFoundException when ID not found")
    void update_NotFound() {
        when(employeeRepository.findById("INVALID"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee("INVALID", testRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Employee not found with ID: INVALID");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("deleteEmployee - should delete when ID exists")
    void delete_Success() {
        when(employeeRepository.findById("EMP2025-0042"))
                .thenReturn(Optional.of(testEmployee));
        doNothing().when(employeeRepository).delete(testEmployee);

        employeeService.deleteEmployee("EMP2025-0042");

        verify(employeeRepository, times(1)).delete(testEmployee);
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("deleteEmployee - should throw EntityNotFoundException when ID not found")
    void delete_NotFound() {
        when(employeeRepository.findById("INVALID"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.deleteEmployee("INVALID"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Employee not found with ID: INVALID");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("searchEmployees - should return matching results")
    void search_ByDepartment() {
        Pageable pageable  = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(List.of(testEmployee));

        when(employeeRepository.searchEmployees(
                isNull(), eq("Engineering"), isNull(), eq(pageable)))
                .thenReturn(page);

        Page<EmployeeResponseDto> result =
                employeeService.searchEmployees(null, "Engineering", null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDepartment()).isEqualTo("Engineering");
    }
}