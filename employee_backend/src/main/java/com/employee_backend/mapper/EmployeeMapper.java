package com.employee_backend.mapper;

import com.employee_backend.dto.EmployeeDto;
import com.employee_backend.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDto mapTOEmployeeDto(Employee employee){
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone()
        );
    }
    public static Employee mapToEmployee(EmployeeDto employeeDto){
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                employeeDto.getPhone()
        );

    }
}
