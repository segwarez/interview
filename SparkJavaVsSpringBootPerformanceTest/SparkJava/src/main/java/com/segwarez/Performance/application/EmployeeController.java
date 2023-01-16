package com.segwarez.Performance.application;

import com.google.gson.Gson;
import com.segwarez.Performance.infrastructure.repository.entity.Employee;
import com.segwarez.Performance.infrastructure.repository.EmployeeRepository;
import com.segwarez.Performance.infrastructure.repository.HibernateEmployeeRepository;
import spark.Request;
import spark.Response;

import java.util.List;

public class EmployeeController {
    private static final String H_LOCATION = "Location";

    private static final Gson gson = new Gson();

    public static List<Employee> getEmployees(Request request, Response response) {
        EmployeeRepository employeeDao = new HibernateEmployeeRepository();
        return employeeDao.findAll();
    }

    public static Employee getEmployee(Request request, Response response) {
        Long id = Long.parseLong(request.params("id"));
        EmployeeRepository employeeDao = new HibernateEmployeeRepository();
        Employee employee = employeeDao.findById(id);
        if (employee == null) {
            response.status(404);
            return null;
        }
        return employee;
    }

    public static String addEmployee(Request request, Response response) {
        EmployeeRepository employeeDao = new HibernateEmployeeRepository();
        Employee employee = getEmployeeFromRequestBody(request);
        Employee persisted = employeeDao.save(employee);
        response.header(H_LOCATION, generateUri(request, persisted.getId()));
        response.status(201);
        return "";
    }

    private static String generateUri(Request request, Long id) {
        String uri = request.url();
        if (uri.matches(".*employee/\\d.*")) {
            uri = uri.substring(0, uri.lastIndexOf("/"));
        }
        return uri + "/" + id;
    }

    private static Employee getEmployeeFromRequestBody(Request request) {
        if (request.body() == null) return null;
        return gson.fromJson(request.body(), Employee.class);
    }
}
