package com.segwarez.Performance;

import com.segwarez.Performance.application.EmployeeController;
import com.segwarez.Performance.infrastructure.configuration.JsonTransformer;

import static spark.Spark.*;

public class PerformanceTestApplication {
    public static void main(String[] args) {
        PerformanceTestApplication performanceTestApplication = new PerformanceTestApplication();
        performanceTestApplication.setup();
    }

    private void setup() {
        get("/employees", EmployeeController::getEmployees, new JsonTransformer());
        get("/employees/:id", EmployeeController::getEmployee, new JsonTransformer());
        post("/employees", EmployeeController::addEmployee);
    }
}
