package com.rev.app.mapper;

import com.rev.app.dto.EmployeeDTO;
import com.rev.app.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {

        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setStatus(employee.getStatus());

        return dto;
    }

    public static Employee toEntity(EmployeeDTO dto) {

        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setStatus(dto.getStatus());

        return employee;
    }
}