package com.blockchain.member.dao;

import org.springframework.stereotype.Repository;

import com.blockchain.base.data.IBaseDao;
import com.blockchain.member.entity.DefaultPermission;

@Repository
public interface DefaultPermissionDao extends IBaseDao<DefaultPermission, Long> {

}
