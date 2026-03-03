package com.rev.app.repository;

import com.rev.app.entity.SystemLog;
import com.rev.app.entity.Employee;
import com.rev.app.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SystemLogRepositoryTest {

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveSystemLog_PersistsRecord() {
        SystemLog log = new SystemLog();
        log.setAction("USER_LOGIN");
        log.setDetails("Employee logged in");
        systemLogRepository.save(log);

        List<SystemLog> all = systemLogRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals("USER_LOGIN", all.get(0).getAction());
    }

    @Test
    void testSaveAndFindById_ReturnsLog() {
        SystemLog log = new SystemLog();
        log.setAction("DATA_EXPORT");
        log.setDetails("Admin exported employee data");
        SystemLog saved = systemLogRepository.save(log);

        assertTrue(systemLogRepository.findById(saved.getLogId()).isPresent());
    }
}
