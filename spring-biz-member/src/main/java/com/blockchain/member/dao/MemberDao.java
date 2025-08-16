package com.blockchain.member.dao;

import org.springframework.stereotype.Repository;

import com.blockchain.base.data.IBaseDao;
import com.blockchain.member.entity.Member;

@Repository
public interface MemberDao extends IBaseDao<Member, Long> {

}
