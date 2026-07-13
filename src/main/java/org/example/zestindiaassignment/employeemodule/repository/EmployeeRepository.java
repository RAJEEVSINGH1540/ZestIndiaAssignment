package org.example.zestindiaassignment.employeemodule.repository;

import org.example.zestindiaassignment.employeemodule.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Employee entity — supports pagination, sorting and search.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);

    Page<Employee> findByDepartment(String department, Pageable pageable);

    Page<Employee> findByStatus(Employee.EmployeeStatus status, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE " +
           "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:department IS NULL OR LOWER(e.department) = LOWER(:department)) AND " +
           "(:status IS NULL OR e.status = :status)")
    Page<Employee> searchEmployees(
            @Param("name")       String name,
            @Param("department") String department,
            @Param("status")     Employee.EmployeeStatus status,
            Pageable pageable
    );
}