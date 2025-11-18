package com.employee_backend.service.impl;
import java.util.Optional;
import java.util.stream.Collectors;
import com.employee_backend.dto.EmployeeDto;
import com.employee_backend.entity.Employee;
import com.employee_backend.exception.EmailAlreadyExistsException;
import com.employee_backend.exception.FieldCannotBeUpdatedException;
import com.employee_backend.exception.PhoneAlreadyExistsException;
import com.employee_backend.exception.ResourceNotFoundException;
import com.employee_backend.mapper.EmployeeMapper;
import com.employee_backend.repository.EmployeeRepository;
import com.employee_backend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employeeDto.getEmail());
        if (existingEmployee.isPresent()) {
            throw new EmailAlreadyExistsException("Employee already exists with given email: " + employeeDto.getEmail());
        }
        Optional<Employee> existingEmployeePhone = employeeRepository.findByPhone(employeeDto.getPhone());
        if (existingEmployeePhone.isPresent()) {
            throw new PhoneAlreadyExistsException("Employee already exists with given phone: " + employeeDto.getPhone());
        }
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee saveedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapTOEmployeeDto(saveedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        // Find the employee in the database, or throw our new exception if not found
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + employeeId));

        // Convert the entity to a DTO and return it
        return EmployeeMapper.mapTOEmployeeDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        // Get the list of all employees from the database
        List<Employee> employees = employeeRepository.findAll();

        // Convert the list of entities to a list of DTOs and return it
        return employees.stream()
                .map(EmployeeMapper::mapTOEmployeeDto)
                .collect(Collectors.toList());
    }

    @Override
    // --- THIS IS THE UPDATED METHOD ---
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployeeDto) {

        // 1. Find the existing employee from the DB
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + employeeId));

        // 2. Check if the client is trying to change the email
        if (!employee.getEmail().equals(updatedEmployeeDto.getEmail())) {
            throw new FieldCannotBeUpdatedException("Email field cannot be updated.");
        }

        // 3. Check if the client is trying to change the phone
        if (!employee.getPhone().equals(updatedEmployeeDto.getPhone())) {
            throw new FieldCannotBeUpdatedException("Phone field cannot be updated.");
        }

        // 4. Update ONLY the fields that are allowed to change
        employee.setFirstName(updatedEmployeeDto.getFirstName());
        employee.setLastName(updatedEmployeeDto.getLastName());

        // 5. Save the employee with updated name fields
        Employee updatedEmployeeObj = employeeRepository.save(employee);

        return EmployeeMapper.mapTOEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        // First, check if the employee exists
        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + employeeId));

        // Delete the employee
        employeeRepository.deleteById(employeeId);
    }
}
