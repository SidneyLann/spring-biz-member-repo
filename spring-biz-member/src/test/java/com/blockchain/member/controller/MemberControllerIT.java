package com.blockchain.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.blockchain.common.base.OpResult;
import com.blockchain.common.dto.member.MemberDto;
import com.blockchain.common.path.MemberPaths;
import com.fasterxml.jackson.databind.ObjectMapper;

// Loads the full application context
@SpringBootTest
// Configures MockMvc automatically
@AutoConfigureMockMvc
// Activates the "test" profile, which can point to an H2 database
@ActiveProfiles("test")
// Rolls back database operations after each test method
@Transactional
public class MemberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to/from JSON

    // Test: Successful member creation
    @Test
    public void createMember_Success() throws Exception {
        // 1. Arrange: Create a MemberDto
        MemberDto memberDto = new MemberDto();
        memberDto.setLoginName("testuser");
        memberDto.setPassword("P@ssw0rd123");
        memberDto.setSmsCode("123456"); // Assume this matches the cached value

        String requestBody = objectMapper.writeValueAsString(memberDto);

        // 2. Act & Assert: Perform POST request and verify expectations
        mockMvc.perform(post(MemberPaths.MEMBER_BASE + MemberPaths.MEMBER_CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("weToken", "valid-token")) // Mock a valid token
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.code").value(OpResult.CODE_COMM_0_SUCCESS)); // Expect success code
    }

    // Test: Create member fails with invalid token
    @Test
    public void createMember_InvalidToken_Failure() throws Exception {
        MemberDto memberDto = new MemberDto();
        memberDto.setLoginName("testuser");
        memberDto.setPassword("P@ssw0rd123");
        memberDto.setSmsCode("123456");

        String requestBody = objectMapper.writeValueAsString(memberDto);

        mockMvc.perform(post(MemberPaths.MEMBER_BASE + MemberPaths.MEMBER_CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("weToken", "invalid-token")) // Invalid token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(OpResult.CODE_AUTH_VALIDATION_SMS_FAIL));
    }
}