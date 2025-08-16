package com.blockchain.member.controller;

import java.util.List;

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
import com.blockchain.common.path.MemberPaths;
import com.blockchain.common.values.CommonValues;
import com.blockchain.member.entity.DefaultPermission;
import com.blockchain.member.entity.SubjectPermission;
import com.blockchain.member.service.DefaultPermissionService;
import com.blockchain.member.service.SubjectPermissionService;

import jakarta.annotation.Resource;

@RestController
public class MemberPermissionController extends DataBaseController {
  private static final Logger LOG = LoggerFactory.getLogger(MemberPermissionController.class);

  @Resource
  private DefaultPermissionService defaultPermissionService;

  @Resource
  private SubjectPermissionService permissionService;

  @PostMapping(path = MemberPaths.PERMISSIN_MB_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult save(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType, @RequestHeader(required = false) String authorities, @RequestBody SubjectPermission permission) {
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

  @PostMapping(path = MemberPaths.PERMISSIN_MB_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult update(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType, @RequestHeader(required = false) String authorities, @RequestBody SubjectPermission permission) {
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

  @PostMapping(path = MemberPaths.PERMISSIN_MB_LOAD)
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

  @PostMapping(path = MemberPaths.PERMISSIN_MB_SEARCH)
  public OpResult search(@RequestParam Long userId) {
    OpResult opResult = new OpResult();
    try {
      List<DefaultPermission> userPermissions = permissionService.search(CommonValues.ORG_TYPE_MB, userId);

      opResult.setBody(userPermissions);
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause(); this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }
}
