package org.example.zestindiaassignment.employeemodule.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for employee data returned in API responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {

    private String      empId;
    private String      name;
    private String      email;
    private String      department;
    private String      position;
    private BigDecimal  salary;
    private LocalDate   dateOfJoining;
    private String      status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}