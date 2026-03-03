package com.rev.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rev.app.dto.LeaveDto;
import com.rev.app.service.LeaveService;
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
class LeaveRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaveService leaveService;

    @Autowired
    private ObjectMapper objectMapper;

    private LeaveDto leaveDto;

    @BeforeEach
    void setUp() {
        leaveDto = new LeaveDto();
        leaveDto.setLeaveId(1L);
        leaveDto.setStatus("PENDING");
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testGetEmployeeLeaves() throws Exception {
        when(leaveService.getEmployeeLeaves(eq("E123"), eq(0), eq(10), eq("startDate")))
                .thenReturn(new PageImpl<>(Collections.singletonList(leaveDto)));

        mockMvc.perform(get("/api/leaves/employee/E123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].leaveId").value(1))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testApplyLeave() throws Exception {
        when(leaveService.applyLeave(any(LeaveDto.class))).thenReturn(leaveDto);

        mockMvc.perform(post("/api/leaves/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leaveId").value(1));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void testApproveLeave() throws Exception {
        leaveDto.setStatus("APPROVED");
        when(leaveService.approveLeave(eq(1L), eq("M123"), eq("Looks good"))).thenReturn(leaveDto);

        mockMvc.perform(put("/api/leaves/approve/1")
                .param("managerId", "M123")
                .param("comment", "Looks good"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
