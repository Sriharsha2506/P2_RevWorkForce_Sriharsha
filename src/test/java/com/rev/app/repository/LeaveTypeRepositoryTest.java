package com.rev.app.repository;

import com.rev.app.entity.LeaveType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LeaveTypeRepositoryTest {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Test
    void testSaveLeaveType_PersistsRecord() {
        LeaveType lt = new LeaveType();
        lt.setLeaveName("Casual");
        lt.setMaxPerYear(12);
        LeaveType saved = leaveTypeRepository.save(lt);

        assertNotNull(saved.getLeaveTypeId());
        assertEquals("Casual", saved.getLeaveName());
    }

    @Test
    void testFindById_ExistingRecord_Returns() {
        LeaveType lt = new LeaveType();
        lt.setLeaveName("Sick");
        lt.setMaxPerYear(10);
        LeaveType saved = leaveTypeRepository.save(lt);

        Optional<LeaveType> found = leaveTypeRepository.findById(saved.getLeaveTypeId());
        assertTrue(found.isPresent());
        assertEquals("Sick", found.get().getLeaveName());
    }

    @Test
    void testFindAll_ReturnsAllLeaveTypes() {
        LeaveType lt1 = new LeaveType();
        lt1.setLeaveName("Annual");
        lt1.setMaxPerYear(15);
        leaveTypeRepository.save(lt1);

        LeaveType lt2 = new LeaveType();
        lt2.setLeaveName("Maternity");
        lt2.setMaxPerYear(90);
        leaveTypeRepository.save(lt2);

        List<LeaveType> all = leaveTypeRepository.findAll();
        assertTrue(all.size() >= 2);
    }
}
