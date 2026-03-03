package com.rev.app.service;

import com.rev.app.entity.Department;
import com.rev.app.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ConfigServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private ConfigServiceImpl configService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDepartments() {
        Department d1 = new Department();
        d1.setDepartmentId(1L);
        d1.setDepartmentName("IT");

        Department d2 = new Department();
        d2.setDepartmentId(2L);
        d2.setDepartmentName("HR");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<Department> result = configService.getAllDepartments();

        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }
}
