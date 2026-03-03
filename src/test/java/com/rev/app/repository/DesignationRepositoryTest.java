package com.rev.app.repository;

import com.rev.app.entity.Designation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DesignationRepositoryTest {

    @Autowired
    private DesignationRepository designationRepository;

    @Test
    void testSaveAndFindAll_ReturnsSavedDesignations() {
        Designation d1 = new Designation();
        d1.setDesignationName("Software Engineer");
        designationRepository.save(d1);

        Designation d2 = new Designation();
        d2.setDesignationName("Senior Engineer");
        designationRepository.save(d2);

        List<Designation> all = designationRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    void testDeleteDesignation_RemovesFromDB() {
        Designation d = new Designation();
        d.setDesignationName("To Delete");
        Designation saved = designationRepository.save(d);

        designationRepository.deleteById(saved.getDesignationId());

        assertFalse(designationRepository.findById(saved.getDesignationId()).isPresent());
    }
}
