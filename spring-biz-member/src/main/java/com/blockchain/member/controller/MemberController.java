/*
*  ******************************************************************************
*  *
*  *
*  * This program and the accompanying materials are made available under the
*  * terms of the Apache License, Version 2.0 which is available at
*  * https://www.apache.org/licenses/LICENSE-2.0.
*  *
*  *  See the NOTICE file distributed with this work for additional
*  *  information regarding copyright ownership.
*  * Unless required by applicable law or agreed to in writing, software
*  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
*  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
*  * License for the specific language governing permissions and limitations
*  * under the License.
*  *
*  * SPDX-License-Identifier: Apache-2.0
*  *****************************************************************************
*/

package com.blockchain.member.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.base.data.CacheClient;
import com.blockchain.base.data.DataBaseController;
import com.blockchain.common.base.OpResult;
import com.blockchain.common.constants.CacheKeys;
import com.blockchain.common.dto.member.MemberDto;
import com.blockchain.common.path.MemberPaths;
import com.blockchain.common.values.CommonValues;
import com.blockchain.common.values.MbValues;
import com.blockchain.common.values.Permissions;
import com.blockchain.member.entity.Member;
import com.blockchain.member.service.MemberService;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * REST Controller for managing member operations including creation, update,
 * deletion, and retrieval of member information.
 * 
 * <p>
 * This controller handles all member-related operations and integrates with the
 * Spring Security framework for authentication and authorization.
 * </p>
 * 
 * @author Blockchain Team
 * @version 1.0
 * @since 2023
 */
@RestController
@RequestMapping(MemberPaths.MEMBER_BASE)
public class MemberController extends DataBaseController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	/** Default password for reset operations */
	private static final String PASSWORD_RESET_DEFAULT = "P@ssw0rd";

	/** Strength parameter for BCrypt password encoding */
	private static final int PASSWORD_ENCODING_STRENGTH = 11;

	/** Threshold for image validation tolerance */
	private static final int IMAGE_VALIDATION_THRESHOLD = 5;

	@Resource
	private MemberService memberService;

	@Resource
	private CacheClient cacheClient;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(PASSWORD_ENCODING_STRENGTH);

	/**
	 * Initializes member field and form configurations for UI rendering.
	 *
	 * @param memberId    the ID of the current member (from header)
	 * @param orgId       the organization ID (from header)
	 * @param orgType     the organization type (from header)
	 * @param authorities the authorities/roles of the current user (from header)
	 * @param apiType     the API type for field generation (default: 1)
	 * @return ResponseEntity containing field and form JavaScript configurations
	 */
	@GetMapping(MemberPaths.MEMBER_INIT)
	public OpResult initializeMemberFields(@RequestHeader(required = false) Long memberId,
			@RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
			@RequestHeader(required = false) String authorities,
			@RequestParam(name = "apiType", defaultValue = "1") int apiType) {

		logger.debug("Initializing member fields with apiType: {}", apiType);

		OpResult opResult = new OpResult();
		try {
			Map<String, Object> initMap = new HashMap<>();
			initMap.put("fieldJs", genFieldJs(Member.class, apiType));
			initMap.put("formJs", genFormJs(Member.class, apiType));

			opResult.setBody(initMap);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.debug("Generated password for default: {}", passwordEncoder.encode(PASSWORD_RESET_DEFAULT));

		} catch (Exception e) {
			handleException(e, opResult);
			return opResult;
		}

		return opResult;
	}

	/**
	 * Creates a new member account after validating registration tokens.
	 *
	 * @param weToken the WeChat token for validation (from header)
	 * @param member  the member data to create
	 * @return ResponseEntity with operation result
	 */
	@PostMapping(value = MemberPaths.MEMBER_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public OpResult createMember(@RequestHeader(value = "weToken", required = false) String weToken,
			@RequestBody Member member) {

		logger.debug("Creating new member with loginName: {}", member.getLoginName());

		OpResult opResult = new OpResult();
		try {
			// Token validation
			String weTokenCache = cacheClient.getString("weToken-" + member.getWeId());
			String smsCodeCache = cacheClient.getString("register-" + member.getLoginName());

			if ((StringUtils.isNotBlank(weToken) && !weToken.equals(weTokenCache))
					|| (StringUtils.isNotBlank(member.getSmsCode()) && !member.getSmsCode().equals(smsCodeCache))) {
				opResult.setCode(OpResult.CODE_AUTH_VALIDATION_SMS_FAIL);
				return opResult;
			}

			member.setId(idGenerator.genId());
			member.setPhone(member.getLoginName());
			member.setPassword(passwordEncoder.encode(member.getPassword()));
			member.setOrgId(MbValues.MEMBER_ORG_ID);
			member.setOrgType(CommonValues.ORG_TYPE_MB);
			member.setRegionId(0L);
			member.setSex(true);
			member.setBirthDay(new Date());

			memberService.save(member);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Member created successfully with ID: {}", member.getId());

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Updates an existing member's information.
	 *
	 * @param memberId    the ID of the current member (from header)
	 * @param orgId       the organization ID (from header)
	 * @param orgType     the organization type (from header)
	 * @param authorities the authorities/roles of the current user (from header)
	 * @param member      the updated member data
	 * @return ResponseEntity with operation result
	 */
	@PutMapping(value = MemberPaths.MEMBER_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public OpResult updateMember(@RequestHeader(required = false) Long memberId,
			@RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
			@RequestHeader(required = false) String authorities, @RequestBody Member member) {

		logger.debug("Updating member with ID: {}", member.getId());

		OpResult opResult = new OpResult();
		try {
			// Permission check
			handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.MB_BASIC);
			if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS) {
				return opResult;
			}

			if (member.getPassword().length() < PASSWORD_ENCODING_STRENGTH + 8) {
				member.setPassword(passwordEncoder.encode(member.getPassword()));
			}

			memberService.update(member);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Member updated successfully with ID: {}", member.getId());

		} catch (Exception e) {
			handleException(e, opResult);
			return opResult;
		}

		return opResult;
	}

	/**
	 * Deletes multiple members by their IDs.
	 *
	 * @param memberId    the ID of the current member (from header)
	 * @param orgId       the organization ID (from header)
	 * @param orgType     the organization type (from header)
	 * @param authorities the authorities/roles of the current user (from header)
	 * @param ids         the list of member IDs to delete
	 * @return ResponseEntity with operation result and count of deleted records
	 */
	@DeleteMapping(value = MemberPaths.MEMBER_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public OpResult deleteMembers(@RequestHeader(required = false) Long memberId,
			@RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
			@RequestHeader(required = false) String authorities, @RequestBody List<Long> ids) {

		logger.debug("Deleting members with IDs: {}", ids);

		OpResult opResult = new OpResult();
		try {
			// Permission check
			handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.MB_BASIC);
			if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS) {
				return opResult;
			}

			int deletedCount = memberService.delete(ids);
			opResult.setTotalRecords(deletedCount);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Deleted {} members successfully", deletedCount);

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Removes a member from an organization (requires elevated permissions).
	 *
	 * @param memberId    the ID of the current member (from header)
	 * @param orgId       the organization ID (from header)
	 * @param orgType     the organization type (from header)
	 * @param authorities the authorities/roles of the current user (from header)
	 * @param id          the ID of the member to remove from the organization
	 * @return ResponseEntity with operation result and count of removed records
	 */
	@DeleteMapping(MemberPaths.MEMBER_ORG_DELETE)
	public OpResult removeMemberFromOrganization(@RequestHeader(required = false) Long memberId,
			@RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
			@RequestHeader(required = false) String authorities, @RequestParam @NotNull Long id) {

		logger.debug("Removing member with ID: {} from organization", id);

		OpResult opResult = new OpResult();
		try {
			// Permission check with multiple allowed permissions
			handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.HQ_ROOT, Permissions.OP_ROOT,
					Permissions.SP_ROOT_G, Permissions.SP_ROOT_R, Permissions.SS_ROOT);
			if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS) {
				return opResult;
			}

			int removalCount = memberService.remove(id);
			opResult.setTotalRecords(removalCount);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Member with ID: {} removed from organization successfully", id);

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Handles password recovery request with image verification.
	 *
	 * @param loginName the login name of the member
	 * @param serialNo  the serial number for image verification
	 * @param topLeftX  the X coordinate for image verification
	 * @return ResponseEntity with operation result
	 */
	@PostMapping(MemberPaths.MEMBER_FIND_PASSWORD)
	public OpResult findPassword(@RequestParam @NotBlank String loginName, @RequestParam @NotBlank String serialNo,
			@RequestParam int topLeftX) {

		logger.debug("Password recovery requested for loginName: {}", loginName);

		OpResult opResult = new OpResult();
		try {
			Member member = memberService.login(loginName, null);
			if (member == null) {
				logger.warn("Password recovery failed: Member not found with loginName: {}", loginName);
				opResult.setCode(OpResult.CODE_COMM_GRANT_INVALID_USER);
				return opResult;
			}

			// Image verification validation
			Integer topLeftX0 = cacheClient.getInteger("topLeftX-" + serialNo);
			if (topLeftX0 == null || Math.abs(topLeftX - topLeftX0) > IMAGE_VALIDATION_THRESHOLD) {
				logger.debug("Image validation failed: expected={}, actual={}", topLeftX0, topLeftX);
				opResult.setCode(OpResult.CODE_AUTH_VALIDATION_IMAGE_FAIL);
				return opResult;
			}

			member.setPassword(passwordEncoder.encode("smsCode"));
			memberService.save(member);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Password reset successfully for loginName: {}", loginName);

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Resets the password for the current member.
	 *
	 * @param memberId    the ID of the current member (from header)
	 * @param orgId       the organization ID (from header)
	 * @param orgType     the organization type (from header)
	 * @param authorities the authorities/roles of the current user (from header)
	 * @param newPassword the new password to set
	 * @return ResponseEntity with operation result
	 */
	@PostMapping(MemberPaths.MEMBER_RESET_PASSWORD)
	public OpResult resetPassword(@RequestHeader(required = false) Long memberId,
			@RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
			@RequestHeader(required = false) String authorities, @RequestParam @NotBlank String newPassword) {

		logger.debug("Password reset requested for memberId: {}", memberId);

		OpResult opResult = new OpResult();
		try {
			// Permission check
			handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.MB_BASIC);
			if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS) {
				return opResult;
			}

			memberService.resetPassword(memberId, passwordEncoder.encode(newPassword));
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Password reset successfully for memberId: {}", memberId);

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Handles member login authentication.
	 *
	 * @param loginName the login name of the member
	 * @param weId      the WeChat ID (optional)
	 * @param password  the password for authentication
	 * @return ResponseEntity with operation result and member data if successful
	 */
	@PostMapping(value = MemberPaths.MEMBER_SEARCH_LOGIN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public OpResult login(@RequestParam @NotBlank String loginName, @RequestParam(required = false) Long weId,
			@RequestParam @NotBlank String password) {

		logger.debug("Login attempt for loginName: {}", loginName);

		OpResult opResult = new OpResult();
		try {
			Member user = memberService.login(loginName, weId);
			if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
				logger.warn("Login failed for loginName: {}", loginName);
				opResult.setCode(OpResult.CODE_COMM_GRANT_INVALID_USER);
			}

			// Cache organization info
			cacheClient.set(CacheKeys.CURR_ORG_TYPE + user.getId(), user.getOrgType());
			cacheClient.set(CacheKeys.CURR_ORG_ID + user.getId(), user.getOrgId());

			logger.debug("Cached org type: {} and org id: {} for user: {}", user.getOrgType(), user.getOrgId(),
					user.getId());

			// Clear sensitive information before returning
			user.setPassword(null);
			opResult.setBody(user);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

			logger.info("Login successful for memberId: {}", user.getId());

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Retrieves a member by their ID.
	 *
	 * @param id the ID of the member to retrieve
	 * @return ResponseEntity with operation result and member data
	 */
	@GetMapping(MemberPaths.MEMBER_LOAD + "/{id}")
	public OpResult loadMember(@PathVariable Long id) {
		logger.debug("Loading member with ID: {}", id);

		OpResult opResult = new OpResult();
		try {
			Member member = memberService.load(id);
			// Clear sensitive information
			member.setPassword(null);
			opResult.setBody(member);
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}

	/**
	 * Searches for members based on the provided criteria.
	 *
	 * @param memberId    the ID of the current member (from header)
	 * @param orgId       the organization ID (from header)
	 * @param orgType     the organization type (from header)
	 * @param authorities the authorities/roles of the current user (from header)
	 * @param dto         the search criteria DTO
	 * @return ResponseEntity with operation result and search results
	 */
	@PostMapping(value = MemberPaths.MEMBER_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE)
	public OpResult searchMembers(@RequestHeader(required = false) Long memberId,
			@RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
			@RequestHeader(required = false) String authorities, @RequestBody MemberDto dto) {

		logger.debug("Searching members with criteria: {}", dto);

		OpResult opResult = new OpResult();
		try {
			if (dto.getPageNo() != null) {
				opResult.setTotalRecords(memberService.searchCount(dto));
			}

			opResult.setBody(memberService.searchResult(dto));
			opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);

		} catch (Exception e) {
			handleException(e, opResult);
		}

		return opResult;
	}
}