package com.blockchain.member.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.blockchain.common.dto.member.MemberDto;
import com.blockchain.common.path.MemberPaths;
import com.blockchain.member.dao.MemberDao;
import com.blockchain.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest // Loads the full application context
@AutoConfigureMockMvc // Automatically configures MockMvc
@ActiveProfiles("test") // Use a 'test' profile to configure an H2 database
@Transactional // Rolls back the test data after the test
public class MemberApiIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MemberDao memberDao; // Real repository

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void createMember_ShouldPersistToDatabase() throws Exception {
		MemberDto memberDto = new MemberDto();
		memberDto.setLoginName("testuser");
		memberDto.setPassword("P@ssw0rd123");
		memberDto.setSmsCode("123456");

		String requestBody = objectMapper.writeValueAsString(memberDto);

		// This performs the request, which goes through the real controller,
		// service, and DAO, saving to the test database.
		mockMvc.perform(post(MemberPaths.MEMBER_BASE + MemberPaths.MEMBER_CREATE)
				.contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());

		// Assertion: Verify the data was actually saved to the DB
		Member member = memberDao.findAll().iterator().next();
		assertThat(member.getLoginName()).isEqualTo(memberDto.getLoginName());
	}
}