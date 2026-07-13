package org.example.zestindiaassignment.employeemodule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zestindiaassignment.common.response.ApiResponse;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeRequestDto;
import org.example.zestindiaassignment.employeemodule.dto.EmployeeResponseDto;
import org.example.zestindiaassignment.employeemodule.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Employee REST Controller — all endpoints require JWT authentication.
 *
 * ADMIN : POST, PUT, DELETE
 * USER  : GET (list, single, search)
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // -------------------------------------------------------
    // POST /api/employees         → ADMIN only
    // -------------------------------------------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(
            @Valid @RequestBody EmployeeRequestDto dto) {

        log.info("Create employee: {}", dto.getName());
        EmployeeResponseDto response = employeeService.createEmployee(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", response));
    }

    // -------------------------------------------------------
    // GET /api/employees/{empId}  → Any authenticated user
    // -------------------------------------------------------
    @GetMapping("/{empId}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getById(
            @PathVariable String empId) {

        log.info("Get employee: {}", empId);
        return ResponseEntity.ok(
                ApiResponse.success("Employee fetched successfully",
                        employeeService.getEmployeeById(empId))
        );
    }

    // -------------------------------------------------------
    // GET /api/employees          → Any authenticated user
    // Query Params: page, size, sortBy, sortDir
    // -------------------------------------------------------
    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeResponseDto>>> getAll(
            @RequestParam(defaultValue = "0")     int    page,
            @RequestParam(defaultValue = "10")    int    size,
            @RequestParam(defaultValue = "empId") String sortBy,
            @RequestParam(defaultValue = "asc")   String sortDir) {

        log.info("Get all employees - page:{} size:{} sortBy:{} sortDir:{}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(
                ApiResponse.success("Employees fetched successfully",
                        employeeService.getAllEmployees(pageable))
        );
    }

    // -------------------------------------------------------
    // GET /api/employees/search   → Any authenticated user
    // Query Params: name, department, status, page, size, sortBy, sortDir
    // -------------------------------------------------------
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<EmployeeResponseDto>>> search(
            @RequestParam(required = false)       String name,
            @RequestParam(required = false)       String department,
            @RequestParam(required = false)       String status,
            @RequestParam(defaultValue = "0")     int    page,
            @RequestParam(defaultValue = "10")    int    size,
            @RequestParam(defaultValue = "name")  String sortBy,
            @RequestParam(defaultValue = "asc")   String sortDir) {

        log.info("Search employees - name:{} dept:{} status:{}", name, department, status);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(
                ApiResponse.success("Search completed",
                        employeeService.searchEmployees(name, department, status, pageable))
        );
    }

    // -------------------------------------------------------
    // PUT /api/employees/{empId}  → ADMIN only
    // -------------------------------------------------------
    @PutMapping("/{empId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> update(
            @PathVariable String empId,
            @Valid @RequestBody EmployeeRequestDto dto) {

        log.info("Update employee: {}", empId);
        return ResponseEntity.ok(
                ApiResponse.success("Employee updated successfully",
                        employeeService.updateEmployee(empId, dto))
        );
    }

    // -------------------------------------------------------
    // DELETE /api/employees/{empId} → ADMIN only
    // -------------------------------------------------------
    @DeleteMapping("/{empId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String empId) {
        log.info("Delete employee: {}", empId);
        employeeService.deleteEmployee(empId);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully"));
    }
}