package com.rev.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.app.dto.EmployeeDto;
import com.rev.app.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employeeDto = new EmployeeDto();
        employeeDto.setEmpId("E123");
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllEmployees() throws Exception {
        when(employeeService.getAllEmployees(eq(0), eq(Integer.MAX_VALUE), eq("firstName")))
                .thenReturn(new PageImpl<>(Collections.singletonList(employeeDto)));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empId").value("E123"))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById("E123")).thenReturn(employeeDto);

        mockMvc.perform(get("/api/employees/E123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value("E123"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateEmployee() throws Exception {
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value("E123"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(any(EmployeeDto.class))).thenReturn(employeeDto);

        mockMvc.perform(put("/api/employees/E123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empId").value("E123"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/E123"))
                .andExpect(status().isOk());
    }
}
