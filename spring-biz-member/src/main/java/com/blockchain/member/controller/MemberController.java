package com.blockchain.member.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;

@RestController
public class MemberController extends DataBaseController {
  private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
  @Resource
  private MemberService memberService;
  @Resource
  protected CacheClient cacheClient;
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);

  @GetMapping(path = MemberPaths.MEMBER_INIT)
  public OpResult init(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
      @RequestHeader(required = false) String authorities, @RequestParam(name = "apiType", required = false, defaultValue = "1") int apiType) {
    OpResult opResult = new OpResult();
    try {
      Map<String, Object> initMap = new HashMap<>();
      initMap.put("fieldJs", this.genFieldJs(Member.class, apiType));
      initMap.put("formJs", this.genFormJs(Member.class, apiType));
      opResult.setBody(initMap);
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
      LOG.debug( "pw: {}", passwordEncoder.encode("P@ssw0rd"));
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult create(@RequestHeader(value = "weToken", required = false) String weToken, @RequestBody Member member) {
    OpResult opResult = new OpResult();
    try {
      String weTokenCache = cacheClient.getString("weToken-" + member.getWeId());
      LOG.debug(weToken + " weToken " + weTokenCache);

      String smsCode = member.getSmsCode();
      String smsCodeCache = cacheClient.getString("register-" + member.getLoginName());
      LOG.debug(member.getSmsCode() + " smsCode " + smsCodeCache);

      if ((StringUtils.isNotBlank(weToken) && !weToken.equals(weTokenCache)) || (StringUtils.isNotBlank(smsCode) && !smsCode.equals(smsCodeCache))) {
        opResult.setCode(OpResult.CODE_AUTH_VALIDATION_SMS_FAIL);
        return opResult; // temp remove checking sms logic when register
      }
      
      member.setId(idGenerator.genId());
      member.setPhone(member.getLoginName());
      member.setPassword(passwordEncoder.encode(member.getPassword()));
      member.setOrgId(MbValues.MEMBER_ORG_ID);
      member.setOrgType(CommonValues.ORG_TYPE_MB);
      member.setRegionId(0l);
      member.setSex(true);
      member.setBirthDay(new Date());

      memberService.save(member);

      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult update(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
      @RequestHeader(required = false) String authorities, @RequestBody Member member) {
    OpResult opResult = new OpResult();
    try {
      this.handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.MB_BASIC);
      if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS)
        return opResult;

      if (member.getPassword().length() < 19) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
      }

      memberService.update(member);
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult delete(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
      @RequestHeader(required = false) String authorities, @RequestBody List<Long> ids) {
    OpResult opResult = new OpResult();
    try {
      this.handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.MB_BASIC);
      if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS)
        return opResult;

      opResult.setTotalRecords(memberService.delete(ids));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_ORG_DELETE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public OpResult remove(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
      @RequestHeader(required = false) String authorities, @RequestParam Long id) {
    OpResult opResult = new OpResult();
    try {
      this.handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.HQ_ROOT, Permissions.OP_ROOT, Permissions.SP_ROOT_G, Permissions.SP_ROOT_R, Permissions.SS_ROOT);
      if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS)
        return opResult;

      opResult.setTotalRecords(memberService.remove(id));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_FIND_PASSWORD, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public OpResult findPassword(@RequestParam String loginName, @RequestParam String serialNo, @RequestParam int topLeftX) {
    OpResult opResult = new OpResult();
    OpResult opResult2;
    try {
      Member member = memberService.login(loginName, null);
      if (member == null) {
        opResult.setCode(OpResult.CODE_COMM_GRANT_INVALID_USER);
        return opResult;
      }

      Integer topLeftX0 = cacheClient.getInteger("topLeftX-" + serialNo);
      if (topLeftX0 == null || Math.abs(topLeftX - topLeftX0) > 5) {
        LOG.debug("topLeftX0: {}", topLeftX0);
        opResult.setCode(OpResult.CODE_AUTH_VALIDATION_IMAGE_FAIL);
        return opResult;
      }

      member.setPassword(passwordEncoder.encode("smsCode"));
      memberService.save(member);
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_RESET_PASSWORD, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public OpResult resetPassword(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
      @RequestHeader(required = false) String authorities, @RequestParam String newPassword) {
    OpResult opResult = new OpResult();
    try {
      this.handlePermission(opResult, memberId, orgId, orgType, authorities, Permissions.MB_BASIC);
      if (opResult.getCode() != OpResult.CODE_COMM_0_SUCCESS)
        return opResult;

      LOG.debug("resetPassword memberId: {}", memberId);

      memberService.resetPassword(memberId, passwordEncoder.encode(newPassword));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_SEARCH_LOGIN, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public OpResult login(@RequestParam String loginName, @RequestParam(required = false) Long weId, @RequestParam String password) {
    OpResult opResult = new OpResult();
    try {
      Member user = memberService.login(loginName, weId);
      if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
        opResult.setCode(OpResult.CODE_COMM_GRANT_INVALID_USER);
      } else {
        cacheClient.set(CacheKeys.CURR_ORG_TYPE + user.getId(), user.getOrgType());
        LOG.debug("current org type = " + user.getOrgType());

        cacheClient.set(CacheKeys.CURR_ORG_ID + user.getId(), user.getOrgId());
        LOG.debug(CacheKeys.CURR_ORG_ID + user.getId() + " current org id = " + user.getOrgId());

        opResult.setBody(user);
        opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
      }
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_LOAD)
  public OpResult load(@RequestParam Long id) {
    OpResult opResult = new OpResult();
    try {
      LOG.debug("id: {}", id);
      Member member = memberService.load(id);
      // member.setPassword(null);
      opResult.setBody(memberService.load(id));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }

  @PostMapping(path = MemberPaths.MEMBER_SEARCH, consumes = MediaType.APPLICATION_JSON_VALUE)
  public OpResult search(@RequestHeader(required = false) Long memberId, @RequestHeader(required = false) Long orgId, @RequestHeader(required = false) Short orgType,
      @RequestHeader(required = false) String authorities, @RequestBody MemberDto dto) {
    OpResult opResult = new OpResult();
    try {
      if (dto.getPageNo() != null)
        opResult.setTotalRecords(memberService.searchCount(dto));

      opResult.setBody(memberService.searchResult(dto));
      opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
    } catch (JpaSystemException e) {
      Throwable t = e.getRootCause();
      this.handleJpaSystemException(t, opResult);
    } catch (Exception e) {
      this.handleException(e, opResult);
    }

    return opResult;
  }
}