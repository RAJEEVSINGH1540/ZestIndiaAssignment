package org.example.zestindiaassignment.employeemodule.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeRequestDto;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeResponseDto;
import org.example.zestindiaassignment.employeemodule.entity.Employee;
import org.example.zestindiaassignment.employeemodule.repository.EmployeeRepository;
import org.example.zestindiaassignment.employeemodule.service.EmployeeService;
import org.example.zestindiaassignment.usermodule.utils.IdGeneratorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementation of EmployeeService — full CRUD with ID generation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto dto) {
        log.debug("Creating employee: {}", dto.getEmail());

        // --- Check email uniqueness ---
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Employee with email '" + dto.getEmail() + "' already exists."
            );
        }

        // --- Generate unique ID: EMP2025-XXXX ---
        String empId = IdGeneratorUtil.generate(
                entityManager,
                "EMP",
                "employees",
                "emp_id",
                1000,
                9999
        );

        // --- Resolve status ---
        Employee.EmployeeStatus status = Employee.EmployeeStatus.ACTIVE;
        if (StringUtils.hasText(dto.getStatus())) {
            try {
                status = Employee.EmployeeStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status '{}', defaulting to ACTIVE", dto.getStatus());
            }
        }

        // --- Build and save ---
        Employee employee = Employee.builder()
                .empId(empId)
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .position(dto.getPosition())
                .salary(dto.getSalary())
                .dateOfJoining(dto.getDateOfJoining())
                .status(status)
                .build();

        Employee saved = employeeRepository.save(employee);
        log.info("Employee created. ID: {}", empId);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(String empId) {
        log.debug("Fetching employee: {}", empId);
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found with ID: " + empId
                ));
        return toDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        log.debug("Fetching all employees, page: {}", pageable.getPageNumber());
        return employeeRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> searchEmployees(
            String name, String department, String status, Pageable pageable) {

        log.debug("Search employees - name:{} dept:{} status:{}", name, department, status);

        Employee.EmployeeStatus empStatus = null;
        if (StringUtils.hasText(status)) {
            try {
                empStatus = Employee.EmployeeStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status filter: {}", status);
            }
        }

        return employeeRepository.searchEmployees(
                StringUtils.hasText(name)       ? name       : null,
                StringUtils.hasText(department) ? department : null,
                empStatus,
                pageable
        ).map(this::toDto);
    }

    @Override
    @Transactional
    public EmployeeResponseDto updateEmployee(String empId, EmployeeRequestDto dto) {
        log.debug("Updating employee: {}", empId);

        Employee existing = employeeRepository.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found with ID: " + empId
                ));

        // --- Check email uniqueness (if email changed) ---
        if (!existing.getEmail().equalsIgnoreCase(dto.getEmail())
                && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Email '" + dto.getEmail() + "' is already in use by another employee."
            );
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setDepartment(dto.getDepartment());
        existing.setPosition(dto.getPosition());
        existing.setSalary(dto.getSalary());
        existing.setDateOfJoining(dto.getDateOfJoining());

        if (StringUtils.hasText(dto.getStatus())) {
            try {
                existing.setStatus(
                        Employee.EmployeeStatus.valueOf(dto.getStatus().toUpperCase())
                );
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status on update: {}", dto.getStatus());
            }
        }

        Employee updated = employeeRepository.save(existing);
        log.info("Employee updated: {}", empId);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void deleteEmployee(String empId) {
        log.debug("Deleting employee: {}", empId);
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found with ID: " + empId
                ));
        employeeRepository.delete(employee);
        log.info("Employee deleted: {}", empId);
    }

    // ---- Mapper ----
    private EmployeeResponseDto toDto(Employee e) {
        return EmployeeResponseDto.builder()
                .empId(e.getEmpId())
                .name(e.getName())
                .email(e.getEmail())
                .department(e.getDepartment())
                .position(e.getPosition())
                .salary(e.getSalary())
                .dateOfJoining(e.getDateOfJoining())
                .status(e.getStatus().name())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}