package com.bracso.test.sleuth.webclient;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collection;

@RestController
public class EmployeeController {

    @GetMapping("/employee")
    public Collection<Employee> getAllEmployees() {
        return Employee.allEmployees();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable String id) {
        return Employee.firstEmployee();
    }

    @RequestMapping(value = "/employee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String newEmployee(Employee employee) {
        return "NEW_EMPLOYEE_CREATED";
    }



}
