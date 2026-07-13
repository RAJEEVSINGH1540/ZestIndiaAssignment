package org.example.zestindiaassignment.employeemodule.service;

import org.example.zestindiaassignment.employeemodule.dto.EmployeeRequestDto;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for employee CRUD operations.
 */
public interface EmployeeService {

    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);

    EmployeeResponseDto getEmployeeById(String empId);

    Page<EmployeeResponseDto> getAllEmployees(Pageable pageable);

    Page<EmployeeResponseDto> searchEmployees(
            String name,
            String department,
            String status,
            Pageable pageable
    );

    EmployeeResponseDto updateEmployee(String empId, EmployeeRequestDto requestDto);

    void deleteEmployee(String empId);
}