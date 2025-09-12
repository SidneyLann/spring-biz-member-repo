package com.blockchain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.blockchain.member.entity.Member;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemberServiceIT {

    @Autowired
    private MemberService memberService;

    @Test
    public void login_ValidCredentials_ReturnsMember() {
        // 1. First, create a member via the service or repository
        Member member = new Member();
        member.setLoginName("johndoe");
        member.setPassword("$2a$11$someHashedPassword"); // Pre-hashed password
        member.setPhone("1234567890");
        // ... set other required fields
        // need to save this member first, perhaps via a @BeforeEach method

        // 2. Act: Try to log in (This test assumes the password is already hashed and matches)
        Member result = memberService.login("johndoe", null);

        // 3. Assert
        assertNotNull(result);
        assertEquals("johndoe", result.getLoginName());
    }
}