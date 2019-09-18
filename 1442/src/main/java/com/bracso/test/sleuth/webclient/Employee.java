package com.bracso.test.sleuth.webclient;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Capgemini
 */
public class Employee implements Serializable {

    private String id;

    private String name;

    private Integer age;

    /**
     * Needed for some serialization/deserialization codecs
     */
    public Employee() {
    }

    public Employee(final String id, final String name, final Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public static Employee firstEmployee() {
        return new Employee("1", "Fran", 22);
    }

    public static Employee secondEmployee() {
        return new Employee("2", "David", 30);
    }

    public static Employee thirdEmployee() {
        return new Employee("3", "Jose", 42);
    }

    public static Collection<Employee> allEmployees() {
        return Arrays.asList(Employee.firstEmployee(), Employee.secondEmployee(), Employee.thirdEmployee());
    }

    public static Employee newEmployee() {
        return new Employee(null, "Nuevo", 27);
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", age=" + age + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        return Objects.equals(this.id, other.id);
    }

}
