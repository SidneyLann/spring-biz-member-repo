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

package com.blockchain.member.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blockchain.base.IBaseService;
import com.blockchain.base.data.DataBaseService;
import com.blockchain.common.dto.member.DefaultPermissionDto;
import com.blockchain.common.dto.member.MemberDto;
import com.blockchain.common.util.DateUtil;
import com.blockchain.common.values.CommonValues;
import com.blockchain.member.dao.MemberDao;
import com.blockchain.member.entity.DefaultPermission;
import com.blockchain.member.entity.Member;
import com.blockchain.member.entity.QMember;
import com.blockchain.member.entity.SubjectPermission;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.annotation.Resource;

/**
 * Service implementation for member management operations. Provides CRUD
 * operations, search functionality, and permission management for members.
 * 
 * @author Blockchain Team
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional(readOnly = true)
public class MemberService extends DataBaseService implements IBaseService<Member, Long> {

	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

	// Sort column constants (using bitmask values)
	public static final int SORT_BY_ID = 1;
	public static final int SORT_BY_LOGIN_NAME = 2;
	public static final int SORT_BY_NICK_NAME = 4;
	public static final int SORT_BY_CREATE_TIME = 8;
	public static final int SORT_BY_ORG_ID = 16;
	public static final int SORT_BY_ORG_TYPE = 32;
	public static final int SORT_BY_EMAIL = 64;
	public static final int SORT_BY_PHONE = 128;
	@Resource
	private MemberDao memberDao;

	@Resource
	private DefaultPermissionService defaultPermissionService;

	@Resource
	private SubjectPermissionService subjectPermissionService;

	/**
	 * Saves a new member with default permissions. Ensures WeId uniqueness by
	 * resetting WeId for any existing member with the same WeId.
	 *
	 * @param member the member entity to save
	 * @return the saved member entity
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Member save(Member member) {
		logger.debug("Saving member with loginName: {}", member.getLoginName());

		// Ensure WeId uniqueness
		if (member.getWeId() != null) {
			resetWeIdForExistingMembers(member.getWeId());
		}

		// Assign default permissions
		assignDefaultPermissions(member);

		return memberDao.save(member);
	}

	/**
	 * Updates an existing member.
	 *
	 * @param member the member entity with updated information
	 * @return the updated member entity
	 */
	@Transactional(rollbackFor = Exception.class)
	public Member update(Member member) {
		logger.debug("Updating member with ID: {}", member.getId());
		return memberDao.save(member);
	}

	/**
	 * Resets the password for a specific member.
	 *
	 * @param id          the ID of the member
	 * @param newPassword the new password (already encoded)
	 * @return the number of affected records (should be 1)
	 */
	@Transactional(rollbackFor = Exception.class)
	public int resetPassword(Long id, String newPassword) {
		logger.debug("Resetting password for member ID: {}", id);

		QMember member = QMember.member;
		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(member.id.eq(id));

		long result = jpaQuery.update(member).set(member.password, newPassword).where(whereClause).execute();

		logger.info("Password reset for member ID: {}. Affected records: {}", id, result);
		return (int) result;
	}

	/**
	 * Soft deletes multiple members by marking them as deleted. Physically deletes
	 * members that are already marked as deleted.
	 *
	 * @param ids the list of member IDs to delete
	 * @return the total number of affected records
	 */
	@Transactional(rollbackFor = Exception.class)
	public int delete(List<Long> ids) {
		logger.debug("Deleting members with IDs: {}", ids);

		QMember member = QMember.member;
		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(member.id.in(ids));

		// First physically delete already soft-deleted records
		BooleanBuilder softDeletedClause = new BooleanBuilder(whereClause);
		softDeletedClause.and(member.status.eq(CommonValues.ACTION_DELETED));

		long physicalDeletes = jpaQuery.delete(member).where(softDeletedClause).execute();

		// Then soft delete the remaining records
		long softDeletes = jpaQuery.update(member).set(member.action, CommonValues.ACTION_DELETED).where(whereClause)
				.execute();

		int totalDeletes = (int) (physicalDeletes + softDeletes);
		logger.info("Deleted {} members. Physical: {}, Soft: {}", totalDeletes, physicalDeletes, softDeletes);
		return totalDeletes;
	}

	/**
	 * Removes a member from their organization by resetting orgId and orgType.
	 *
	 * @param id the ID of the member to remove
	 * @return the number of affected records (should be 1)
	 */
	@Transactional(rollbackFor = Exception.class)
	public int remove(Long id) {
		logger.debug("Removing member with ID: {} from organization", id);

		QMember member = QMember.member;
		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(member.id.eq(id));

		long result = jpaQuery.update(member).set(member.orgId, 0L).set(member.orgType, (short) 13).where(whereClause)
				.execute();

		logger.info("Removed member with ID: {} from organization. Affected records: {}", id, result);
		return (int) result;
	}

	/**
	 * Retrieves a member by their ID.
	 *
	 * @param id the ID of the member to retrieve
	 * @return the member entity
	 */
	@Override
	public Member load(Long id) {
		logger.debug("Loading member with ID: {}", id);
		return memberDao.findById(id).orElse(null);
	}

	/**
	 * Builds a query with conditions based on the provided MemberDto.
	 *
	 * @param dto the search criteria
	 * @return a JPAQuery with applied conditions
	 */
	public JPAQuery<Member> searchCondition(MemberDto dto) {
		QMember member = QMember.member;
		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(member.action.lt(CommonValues.ACTION_DELETED));

		// Apply filters based on provided criteria
		if (dto.getId() != null) {
			whereClause.and(member.id.eq(dto.getId()));
		}

		if (dto.getOrgId() != null) {
			whereClause.and(member.orgId.eq(dto.getOrgId()));
		}

		if (dto.getOrgType() != null) {
			whereClause.and(member.orgType.eq(dto.getOrgType()));
		}

		if (dto.getStatus() != null) {
			whereClause.and(member.status.eq(dto.getStatus()));
		}

		if (StringUtils.isNotBlank(dto.getNickName())) {
			whereClause.and(member.nickName.containsIgnoreCase(dto.getNickName()));
		}

		if (StringUtils.isNotBlank(dto.getLoginName())) {
			whereClause.and(member.loginName.containsIgnoreCase(dto.getLoginName()));
		}

		if (StringUtils.isNotBlank(dto.getPhone())) {
			whereClause.and(member.phone.containsIgnoreCase(dto.getPhone()));
		}

		if (StringUtils.isNotBlank(dto.getEmail())) {
			whereClause.and(member.email.containsIgnoreCase(dto.getEmail()));
		}

		if (dto.getCreateTime() != null) {
			whereClause.and(member.createTime.after(DateUtil.getDatePart(dto.getCreateTime())));
		}

		if (dto.getCreateTime2() != null) {
			whereClause.and(member.createTime.before(DateUtil.getNextDatePart(dto.getCreateTime2())));
		}

		logger.debug("Built search query with conditions: {}", whereClause);
		return jpaQuery.selectFrom(member).where(whereClause);
	}

	/**
	 * Counts members matching the search criteria.
	 *
	 * @param dto the search criteria
	 * @return the number of matching members
	 */
	public int searchCount(MemberDto dto) {
		logger.debug("Counting members with criteria: {}", dto);
		long count = searchCondition(dto).fetchCount();
		logger.debug("Found {} members matching criteria", count);
		return (int) count;
	}

	/**
	 * Searches for members based on criteria with pagination support.
	 *
	 * @param dto the search criteria including pagination parameters
	 * @return a list of matching members
	 */
	public List<Member> searchResult(MemberDto dto) {
		logger.debug("Searching members with criteria: {}", dto);

		JPAQuery<Member> query = searchCondition(dto);

		// Apply pagination if requested
		if (dto.getPageNo() != null && dto.getPageSize() != null) {
			int offset = dto.getPageSize() * (dto.getPageNo() - 1);
			query.offset(offset).limit(dto.getPageSize());
		}

		// Apply sorting if specified
		if (dto.getSortBy() != null) {
			applySorting(query, dto.getSortBy(), dto.isAscending());
		}

		List<Member> results = query.fetch();
		logger.debug("Found {} members matching search criteria", results.size());
		return results;
	}

	/**
	 * Searches for members with pagination support and returns a Page object.
	 *
	 * @param dto the search criteria including pagination parameters
	 * @return a Page of matching members
	 */
	public Page<Member> search(MemberDto dto) {
		logger.debug("Searching members with pagination, criteria: {}", dto);

		JPAQuery<Member> query = searchCondition(dto);
		long total = query.fetchCount();

		// Apply pagination
		if (dto.getPageNo() != null && dto.getPageSize() != null) {
			int offset = dto.getPageSize() * (dto.getPageNo() - 1);
			query.offset(offset).limit(dto.getPageSize());
		}

		// Apply sorting if specified
		if (dto.getSortBy() != null) {
			applySorting(query, dto.getSortBy(), dto.isAscending());
		}

		List<Member> content = query.fetch();

		Pageable pageable = org.springframework.data.domain.PageRequest.of(
				dto.getPageNo() != null ? dto.getPageNo() - 1 : 0,
				dto.getPageSize() != null ? dto.getPageSize() : Integer.MAX_VALUE);

		logger.debug("Found {} members out of {} total", content.size(), total);
		return new PageImpl<>(content, pageable, total);
	}

	/**
	 * Authenticates a member by login name and optional WeId.
	 *
	 * @param loginName the login name, phone, or email
	 * @param weId      the optional WeId
	 * @return the authenticated member or null if not found
	 */
	public Member login(String loginName, Long weId) {
		logger.debug("Attempting login for: {}, weId: {}", loginName, weId);

		QMember member = QMember.member;
		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(member.action.lt(CommonValues.ACTION_DELETED));

		// Search by login name, phone, or email
		BooleanBuilder loginCondition = new BooleanBuilder();
		loginCondition.or(member.loginName.equalsIgnoreCase(loginName)).or(member.phone.equalsIgnoreCase(loginName))
				.or(member.email.equalsIgnoreCase(loginName));

		whereClause.and(loginCondition);

		// Add WeId condition if provided
		if (weId != null && !weId.equals(CommonValues.NUM_LONG_ZERO)) {
			whereClause.and(member.weId.eq(weId));
		}

		List<Member> matchingMembers = jpaQuery.selectFrom(member).where(whereClause).fetch();

		if (matchingMembers.isEmpty()) {
			logger.warn("Login failed for: {}. No matching member found", loginName);
			return null;
		}

		if (matchingMembers.size() > 1) {
			logger.warn("Login ambiguous for: {}. Found {} matching members", loginName, matchingMembers.size());
		}

		logger.info("Login successful for: {}", loginName);
		return matchingMembers.get(0);
	}

	/**
	 * Resets WeId for any existing members with the specified WeId.
	 *
	 * @param weId the WeId to reset
	 */
	private void resetWeIdForExistingMembers(Long weId) {
		QMember member = QMember.member;
		BooleanBuilder whereClause = new BooleanBuilder();
		whereClause.and(member.weId.eq(weId));

		long updatedCount = jpaQuery.update(member).set(member.weId, 0L).where(whereClause).execute();

		if (updatedCount > 0) {
			logger.info("Reset WeId for {} existing members with WeId: {}", updatedCount, weId);
		}
	}

	/**
	 * Assigns default permissions to a new member.
	 *
	 * @param member the member to assign permissions to
	 */
	private void assignDefaultPermissions(Member member) {
		DefaultPermissionDto dto = new DefaultPermissionDto();
		dto.setOrgType(CommonValues.ORG_TYPE_MB);

		List<DefaultPermission> defaultPermissions = defaultPermissionService.search(dto);

		for (DefaultPermission defaultPermission : defaultPermissions) {
			SubjectPermission memberPermission = new SubjectPermission();
			memberPermission.setId(idGenerator.genId());
			memberPermission.setOrgType(CommonValues.ORG_TYPE_MB);
			memberPermission.setUserId(member.getId());
			memberPermission.setPermissionId(defaultPermission.getId());

			subjectPermissionService.save(memberPermission);
		}

		logger.debug("Assigned {} default permissions to member ID: {}", defaultPermissions.size(), member.getId());
	}

	/**
	 * Applies sorting to the query based on the specified field bitmask and
	 * direction.
	 *
	 * @param query     the query to apply sorting to
	 * @param sortBy    the bitmask representing which columns to sort by
	 * @param ascending true for ascending order, false for descending
	 */
	private void applySorting(JPAQuery<Member> query, int sortBy, boolean ascending) {
		QMember member = QMember.member;
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
		Order order = ascending ? Order.ASC : Order.DESC;

		// Apply sorting based on bitmask
		if ((sortBy & SORT_BY_ID) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.id));
		}

		if ((sortBy & SORT_BY_LOGIN_NAME) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.loginName));
		}

		if ((sortBy & SORT_BY_NICK_NAME) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.nickName));
		}

		if ((sortBy & SORT_BY_CREATE_TIME) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.createTime));
		}

		if ((sortBy & SORT_BY_ORG_ID) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.orgId));
		}

		if ((sortBy & SORT_BY_ORG_TYPE) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.orgType));
		}

		if ((sortBy & SORT_BY_EMAIL) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.email));
		}

		if ((sortBy & SORT_BY_PHONE) != 0) {
			orderSpecifiers.add(new OrderSpecifier<>(order, member.phone));
		}

		// Apply all sorting criteria
		if (!orderSpecifiers.isEmpty()) {
			query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
		}
	}
}