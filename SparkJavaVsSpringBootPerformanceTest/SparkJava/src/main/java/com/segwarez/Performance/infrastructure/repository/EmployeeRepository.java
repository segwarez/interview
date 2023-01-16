package com.segwarez.Performance.infrastructure.repository;

import com.segwarez.Performance.infrastructure.repository.entity.Employee;

import java.util.List;

public interface EmployeeRepository {
    Employee findById(Long id);

    List<Employee> findAll();

    Employee save(Employee employee);
}
