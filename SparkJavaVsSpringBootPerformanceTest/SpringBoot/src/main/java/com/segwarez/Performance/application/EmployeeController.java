package com.segwarez.Performance.application;

import com.segwarez.Performance.infrastructure.repository.entity.Employee;
import com.segwarez.Performance.infrastructure.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeDao;

    @GetMapping(path = {"/", "/{id}"}, produces = "application/json")
    public ResponseEntity<Object> getEmployee(@PathVariable Optional<Long> id) {
        if (id.isPresent()) {
            return ResponseEntity.ok(employeeDao.findById(id.get()).get());
        } else {
            return ResponseEntity.ok(employeeDao.findAll());
        }
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> addEmployee(@RequestBody Employee em) {
        Employee employee = employeeDao.saveAndFlush(new Employee(em.getFirstName(), em.getLastName(), em.getEmail()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
