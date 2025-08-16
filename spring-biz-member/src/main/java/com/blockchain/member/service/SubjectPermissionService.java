package com.blockchain.member.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blockchain.base.IBaseService;
import com.blockchain.base.data.DataBaseService;
import com.blockchain.common.util.ValueUtil;
import com.blockchain.common.values.CommonValues;
import com.blockchain.member.dao.SubjectPermissionDao;
import com.blockchain.member.entity.DefaultPermission;
import com.blockchain.member.entity.QDefaultPermission;
import com.blockchain.member.entity.QSubjectPermission;
import com.blockchain.member.entity.SubjectPermission;
import com.querydsl.core.BooleanBuilder;

@Service
public class SubjectPermissionService extends DataBaseService implements IBaseService<SubjectPermission, Long> {
  private static final Logger LOG = LoggerFactory.getLogger(SubjectPermissionService.class);

  @Resource
  private SubjectPermissionDao permissionDao;

  @Override
  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public SubjectPermission save(SubjectPermission permission) {
    return permissionDao.save(permission);
  }

  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public void resetPermission(Short orgType, Long userId, List<Long> permissionIds) {
    QSubjectPermission queryEntity = QSubjectPermission.subjectPermission;
    LOG.debug("userId: {}",  userId);
    LOG.debug("orgType: {}",  orgType);
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.userId.eq(userId));
    whereBuilder.and(queryEntity.orgType.eq(orgType));
    jpaQuery.delete(queryEntity).where(whereBuilder);
    // jpaQuery.update(queryEntity).set(queryEntity.action, ValueUtil.getActionValue()).where(whereBuilder);

    List<SubjectPermission> deleteEntity = jpaQuery.select(queryEntity).from(queryEntity).where(whereBuilder).fetch();
    for (SubjectPermission entity : deleteEntity)
      entity.setAction(ValueUtil.getActionValue());
    permissionDao.saveAll(deleteEntity);

    List<SubjectPermission> permissions = new ArrayList<>();
    SubjectPermission permission = null;
    for (Long permissionId : permissionIds) {
      permission = new SubjectPermission();
      permission.setId(idGenerator.genId());
      permission.setOrgType(orgType);
      permission.setUserId(userId);
      permission.setPermissionId(permissionId);

      permissions.add(permission);
    }

    permissionDao.saveAll(permissions);
  }

  @Override
  public SubjectPermission load(Long id) {
    return permissionDao.findById(id).get();
  }

  public List<DefaultPermission> search(Short orgType, Long userId) {
    QDefaultPermission queryEntity = QDefaultPermission.defaultPermission;
    QSubjectPermission queryEntity2 = QSubjectPermission.subjectPermission;

    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.action.lt(CommonValues.ACTION_DELETED));
    whereBuilder.and(queryEntity2.action.lt(CommonValues.ACTION_DELETED));

    if (orgType != null)
      whereBuilder.and(queryEntity.orgType.eq(orgType));

    if (userId != null)
      whereBuilder.and(queryEntity2.userId.eq(userId));

    List<DefaultPermission> resultList = jpaQuery.select(queryEntity).from(queryEntity).innerJoin(queryEntity2).on(queryEntity.id.eq(queryEntity2.permissionId)).where(whereBuilder).fetch();

    return resultList;
  }

}
