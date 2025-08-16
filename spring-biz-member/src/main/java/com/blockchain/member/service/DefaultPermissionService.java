package com.blockchain.member.service;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import com.blockchain.base.data.DataBaseService;
import com.blockchain.base.IBaseService;
import com.blockchain.common.dto.member.DefaultPermissionDto;
import com.blockchain.common.values.CommonValues;
import com.blockchain.member.dao.DefaultPermissionDao;
import com.blockchain.member.entity.DefaultPermission;
import com.blockchain.member.entity.QDefaultPermission;
import com.querydsl.core.BooleanBuilder;

@Service
public class DefaultPermissionService extends DataBaseService implements IBaseService<DefaultPermission, Long> {

  @Resource
  private DefaultPermissionDao permissionDao;
  
  @Override
  @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
  public DefaultPermission save(DefaultPermission spec) {
    return permissionDao.save(spec);
  }

  @Override
  public DefaultPermission load(Long id) {
    return permissionDao.findById(id).get();
  }

  public List<DefaultPermission> search(DefaultPermissionDto dto) {
    QDefaultPermission queryEntity = QDefaultPermission.defaultPermission;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.action.lt(CommonValues.ACTION_DELETED));
    
    if (dto.getOrgType() != null)
      whereBuilder.and(queryEntity.orgType.eq(dto.getOrgType()));

    if (dto.getPermissionName() != null)
      whereBuilder.and(queryEntity.permissionName.contains(dto.getPermissionName()));

    List<DefaultPermission> resultList = jpaQuery.select(queryEntity).from(queryEntity).where(whereBuilder).fetch();

    return resultList;
  }

  public List<DefaultPermission> search(List<Long> permissionIds) {
    QDefaultPermission queryEntity = QDefaultPermission.defaultPermission;
    BooleanBuilder whereBuilder = new BooleanBuilder();
    whereBuilder.and(queryEntity.action.lt(CommonValues.ACTION_DELETED));
    whereBuilder.and(queryEntity.id.in(permissionIds));

    List<DefaultPermission> resultList = jpaQuery.select(queryEntity).from(queryEntity).where(whereBuilder).fetch();

    return resultList;
  }
}
