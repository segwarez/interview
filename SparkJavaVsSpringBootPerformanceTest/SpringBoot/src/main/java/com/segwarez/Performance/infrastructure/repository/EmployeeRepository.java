package com.segwarez.Performance.infrastructure.repository;

import com.segwarez.Performance.infrastructure.repository.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
    List<Employee> findByFirstName(String firstName);
}
