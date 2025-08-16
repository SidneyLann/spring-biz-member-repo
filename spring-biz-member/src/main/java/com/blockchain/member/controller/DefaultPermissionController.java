package com.blockchain.member.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.base.data.DataBaseController;
import com.blockchain.common.base.OpResult;
import com.blockchain.common.dto.member.DefaultPermissionDto;
import com.blockchain.common.path.MemberPaths;
import com.blockchain.member.entity.DefaultPermission;
import com.blockchain.member.service.DefaultPermissionService;

import jakarta.annotation.Resource;

@RestController
public class DefaultPermissionController extends DataBaseController {
  private static final Logger LOG = LoggerFactory.getLogger(DefaultPermissionController.class);

  @Resource
  private DefaultPermissionService permissionService;

  @PostMapping(path = MemberPaths.PERMISSIN_DEFAULT_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult create(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType, @RequestHeader(required = false) String authorities, @RequestBody DefaultPermission permission) {
    OpResult opResult = new OpResult();
    try {
      permission.setId(idGenerator.genId());
      opResult.setBody(permissionService.save(permission));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause(); this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.PERMISSIN_DEFAULT_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult update(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType, @RequestHeader(required = false) String authorities, @RequestBody DefaultPermission permission) {
    OpResult opResult = new OpResult();
    try {
      opResult.setBody(permissionService.save(permission));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause(); this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.PERMISSIN_DEFAULT_LOAD)
  public OpResult load(@RequestParam Long id) {
    OpResult opResult = new OpResult();
    try {
      opResult.setBody(permissionService.load(id));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause(); this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.PERMISSIN_DEFAULT_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult search(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType, @RequestHeader(required = false) String authorities, @RequestBody DefaultPermissionDto dto) {
    OpResult opResult = new OpResult();
    try {
      opResult.setBody(permissionService.search(dto));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause(); this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

}
