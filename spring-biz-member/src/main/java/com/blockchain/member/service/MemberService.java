package com.blockchain.member.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blockchain.base.IBaseService;
import com.blockchain.base.data.DataBaseService;
import com.blockchain.common.dto.member.DefaultPermissionDto;
import com.blockchain.common.dto.member.MemberDto;
import com.blockchain.common.dto.member.PermissionDto;
import com.blockchain.common.util.DateUtil;
import com.blockchain.common.values.CommonValues;
import com.blockchain.member.dao.MemberDao;
import com.blockchain.member.entity.DefaultPermission;
import com.blockchain.member.entity.Member;
import com.blockchain.member.entity.QMember;
import com.blockchain.member.entity.SubjectPermission;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService extends DataBaseService implements IBaseService<Member, Long> {
  private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);
  @Resource
  private MemberDao memberDao;
  @Resource
  private DefaultPermissionService defaultPermissionService;
  @Resource
  private SubjectPermissionService subjectPermissionService;

  @Override
  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public Member save(Member member) {
    if (member.getWeId() != null) {
      QMember queryEntity = QMember.member;
      BooleanBuilder whereBuilder = new BooleanBuilder();
      whereBuilder.and(queryEntity.weId.eq(member.getWeId()));
      jpaQuery.update(queryEntity).set(queryEntity.weId, 0l).where(whereBuilder).execute();
    }

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

    return memberDao.save(member);
  }

  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public Member update(Member member) {
    return memberDao.save(member);
  }

  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public int resetPassword(Long id, String newPassword) {
    QMember queryEntity = QMember.member;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.id.eq(id));

    Long result = jpaQuery.update(queryEntity).set(queryEntity.password, newPassword).where(whereBuilder).execute();

    return result.intValue();
  }


  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public int delete(List<Long> ids) {
    QMember queryEntity = QMember.member;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.id.in(ids));
    whereBuilder.and(queryEntity.status.eq(CommonValues.ACTION_DELETED));
    Long result = jpaQuery.delete(queryEntity).where(whereBuilder).execute();

    whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.id.in(ids));
    result += jpaQuery.update(queryEntity).set(queryEntity.action, CommonValues.ACTION_DELETED).where(whereBuilder).execute();

    return result.intValue();
  }

  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public int remove(Long id) {
    QMember queryEntity = QMember.member;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.id.eq(id));
    Long result = jpaQuery.update(queryEntity).set(queryEntity.orgId, 0l).set(queryEntity.orgType, (short) 13).where(whereBuilder).execute();

    return result.intValue();
  }

  @Override
  public Member load(Long id) {
    return memberDao.findById(id).get();
  }

  public JPAQuery<Member> searchCondition(MemberDto dto) {
    QMember queryEntity = QMember.member;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.action.lt(CommonValues.ACTION_DELETED));

    if (dto.getId() != null)
      whereBuilder.and(queryEntity.id.eq(dto.getId()));

    if (dto.getOrgId() != null)
      whereBuilder.and(queryEntity.orgId.eq(dto.getOrgId()));

    if (dto.getOrgType() != null && (dto.getOrgType() != null))
      whereBuilder.and(queryEntity.orgType.eq(dto.getOrgType()));

    if (dto.getStatus() != null && (dto.getStatus() != null))
      whereBuilder.and(queryEntity.status.eq(dto.getStatus()));

    if (!StringUtils.isBlank(dto.getNickName()))
      whereBuilder.and(queryEntity.nickName.contains(dto.getNickName()));

    if (!StringUtils.isBlank(dto.getLoginName()))
      whereBuilder.and(queryEntity.loginName.contains(dto.getLoginName()));

    if (!StringUtils.isBlank(dto.getPhone()))
      whereBuilder.and(queryEntity.phone.contains(dto.getPhone()));

    if (!StringUtils.isBlank(dto.getEmail()))
      whereBuilder.and(queryEntity.email.contains(dto.getEmail()));

    if (dto.getCreateTime() != null)
      whereBuilder.and(queryEntity.createTime.gt(DateUtil.getDatePart(dto.getCreateTime())));

    if (dto.getCreateTime2() != null)
      whereBuilder.and(queryEntity.createTime.lt(DateUtil.getNextDatePart(dto.getCreateTime2())));

    LOG.debug("whereBuilder: {}", whereBuilder);
    return jpaQuery.select(queryEntity).from(queryEntity).where(whereBuilder);
  }

  public int searchCount(MemberDto dto) {

    return (int) searchCondition(dto).fetch().size();
  }

  public List<Member> searchResult(MemberDto dto) {
    List<Member> resultList = null;
    if (dto.getPageNo() == null)
      resultList = searchCondition(dto).fetch();
    else {
      resultList = searchCondition(dto).offset(dto.getPageSize() * (dto.getPageNo() - 1)).limit(dto.getPageSize()).fetch();
    }

    return resultList;
  }

  public Member login(String loginName, Long weId) {
    LOG.debug("loginName: {}", loginName);
    LOG.debug("weId: {}", weId);
    QMember queryEntity = QMember.member;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    BooleanBuilder whereBuilder2 = new BooleanBuilder();
    whereBuilder.and(queryEntity.action.lt(CommonValues.ACTION_DELETED));
    whereBuilder2.or(queryEntity.loginName.eq(loginName));
    whereBuilder2.or(queryEntity.phone.eq(loginName));
    whereBuilder2.or(queryEntity.email.eq(loginName));
    whereBuilder.and(whereBuilder2);
    if (weId != null && weId != CommonValues.NUM_LONG_ZERO) {
      whereBuilder.and(queryEntity.weId.eq(weId));
    }

    List<Member> matchMembers = jpaQuery.select(queryEntity).from(queryEntity).where(whereBuilder).fetch();

    if (matchMembers.size() > 0) {
      return matchMembers.get(0);
    }

    LOG.debug("matchMembers size is: {}", matchMembers.size());

    return null;
  }
}
