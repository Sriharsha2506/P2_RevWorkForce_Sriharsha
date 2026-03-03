package com.rev.app.repository;

import com.rev.app.entity.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void testSaveAndFindById() {
        Department dept = new Department();
        dept.setDepartmentName("Engineering");

        Department saved = departmentRepository.save(dept);
        assertThat(saved.getDepartmentId()).isNotNull();

        Optional<Department> found = departmentRepository.findById(saved.getDepartmentId());
        assertThat(found).isPresent();
        assertThat(found.get().getDepartmentName()).isEqualTo("Engineering");
    }

    @Test
    public void testFindAll() {
        Department d1 = new Department();
        d1.setDepartmentName("Sales");
        departmentRepository.save(d1);

        Department d2 = new Department();
        d2.setDepartmentName("Marketing");
        departmentRepository.save(d2);

        List<Department> deps = departmentRepository.findAll();
        // H2 data.sql might already populate 5 departments, plus these 2 = 7+
        assertThat(deps.size()).isGreaterThanOrEqualTo(2);
    }
}
