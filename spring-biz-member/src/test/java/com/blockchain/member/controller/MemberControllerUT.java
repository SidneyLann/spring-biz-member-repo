package com.blockchain.member.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blockchain.base.data.CacheClient;
import com.blockchain.base.data.IdGenerator;
import com.blockchain.common.base.OpResult;
import com.blockchain.common.dto.member.MemberDto;
import com.blockchain.common.values.CommonValues;
import com.blockchain.common.values.MbValues;
import com.blockchain.member.entity.Member;
import com.blockchain.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
class MemberControllerUT {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MemberService memberService;
    
    @Mock
    protected IdGenerator idGenerator;
    
    @Mock
    private CacheClient cacheClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberController memberController;

    private Member testMember;
    private MemberDto testMemberDto;
    private final Long testMemberId = 1L;

    @BeforeEach
    void setUp() {
        // Set up test data
        testMember = new Member();
        testMember.setId(testMemberId);
        testMember.setLoginName("testuser");
        testMember.setPassword("encodedPassword");
        testMember.setPhone("1234567890");
        testMember.setOrgId(MbValues.MEMBER_ORG_ID);
        testMember.setOrgType(CommonValues.ORG_TYPE_MB);
        testMember.setRegionId(0L);
        testMember.setSex(true);
        testMember.setBirthDay(new Date());

        testMemberDto = new MemberDto();
        testMemberDto.setId(testMemberId);
        testMemberDto.setLoginName("testuser");
        testMemberDto.setPassword("rawPassword");
    }

    @Test
    void testCreateMember_Success() {
    	cacheClient.set("weToken-" + testMember.getWeId(), "validToken");
    	
        // Arrange
        when(modelMapper.map(any(MemberDto.class), eq(Member.class))).thenReturn(testMember);
        when(cacheClient.getString(eq("weToken-0"))).thenReturn("validToken");
        when(memberService.save(any(Member.class))).thenReturn(testMember);

        // Act
        OpResult result = memberController.createMember("validToken", testMemberDto);

        // Assert
        assertEquals(OpResult.CODE_COMM_0_SUCCESS, result.getCode());
        verify(memberService, times(1)).save(any(Member.class));
    }

    //@Test
    void testCreateMember_InvalidToken() {
        // Arrange
        when(modelMapper.map(any(MemberDto.class), eq(Member.class))).thenReturn(testMember);
//        when(cacheClient.getString(eq("weToken-123"))).thenReturn("differentToken");

        // Act
        OpResult result = memberController.createMember("invalidToken", testMemberDto);

        // Assert
        assertEquals(OpResult.CODE_AUTH_VALIDATION_SMS_FAIL, result.getCode());
        verify(memberService, never()).save(any(Member.class));
    }

    //@Test
    void testUpdateMember_Success() {
        // Arrange
        when(modelMapper.map(any(MemberDto.class), eq(Member.class))).thenReturn(testMember);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(memberService.update(any(Member.class))).thenReturn(testMember);

        // Act
        OpResult result = memberController.updateMember(testMemberId, MbValues.MEMBER_ORG_ID, 
                CommonValues.ORG_TYPE_MB, "MB_BASIC", testMemberDto);

        // Assert
        assertEquals(OpResult.CODE_COMM_0_SUCCESS, result.getCode());
        verify(memberService, times(1)).update(any(Member.class));
    }

   // @Test
    void testLogin_Success() {
        // Arrange
        when(memberService.login(eq("testuser"), anyLong())).thenReturn(testMember);
        when(passwordEncoder.matches(eq("correctPassword"), eq("encodedPassword"))).thenReturn(true);

        // Act
        OpResult result = memberController.login("testuser", 123L, "correctPassword");

        // Assert
        assertEquals(OpResult.CODE_COMM_0_SUCCESS, result.getCode());
        assertNotNull(result.getBody());
        verify(cacheClient, times(1)).set(anyString(), any());
    }

    //@Test
    void testLogin_InvalidCredentials() {
        // Arrange
        when(memberService.login(eq("testuser"), anyLong())).thenReturn(testMember);
        when(passwordEncoder.matches(eq("wrongPassword"), eq("encodedPassword"))).thenReturn(false);

        // Act
        OpResult result = memberController.login("testuser", 123L, "wrongPassword");

        // Assert
        assertEquals(OpResult.CODE_COMM_GRANT_INVALID_USER, result.getCode());
    }

    //@Test
    void testLoadMember_Success() {
        // Arrange
        when(memberService.load(eq(testMemberId))).thenReturn(testMember);
        when(modelMapper.map(any(Member.class), eq(MemberDto.class))).thenReturn(testMemberDto);

        // Act
        OpResult result = memberController.loadMember(testMemberId);

        // Assert
        assertEquals(OpResult.CODE_COMM_0_SUCCESS, result.getCode());
        assertNotNull(result.getBody());
        verify(memberService, times(1)).load(testMemberId);
    }

   // @Test
    void testSearchMembers_Success() {
        // Arrange
        MemberDto searchCriteria = new MemberDto();
        searchCriteria.setPageNo((short)1);
        
        List<Member> memberList = Arrays.asList(testMember);
        when(memberService.searchCount(any(MemberDto.class))).thenReturn(1);
        when(memberService.searchResult(any(MemberDto.class))).thenReturn(memberList);
        when(modelMapper.map(any(Member.class), eq(MemberDto.class))).thenReturn(testMemberDto);

        // Act
        OpResult result = memberController.searchMembers(testMemberId, MbValues.MEMBER_ORG_ID, 
                CommonValues.ORG_TYPE_MB, "MB_BASIC", searchCriteria);

        // Assert
        assertEquals(OpResult.CODE_COMM_0_SUCCESS, result.getCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getTotalRecords());
    }

   // @Test
    void testExceptionHandling() {
        // Arrange
        when(memberService.load(eq(testMemberId))).thenThrow(new RuntimeException("Test exception"));

        // Act
        OpResult result = memberController.loadMember(testMemberId);

        // Assert
        assertNotEquals(OpResult.CODE_COMM_0_SUCCESS, result.getCode());
    }
}