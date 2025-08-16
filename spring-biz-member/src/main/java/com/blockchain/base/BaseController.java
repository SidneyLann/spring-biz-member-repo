package com.blockchain.base;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blockchain.common.base.BizzException;
import com.blockchain.common.base.OpResult;
import com.blockchain.common.values.CommonValues;

public abstract class BaseController {
  private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

  protected void handleException(Exception e, OpResult opResult) {
    opResult.setCode(OpResult.CODE_COMM_EXCEPTION);
    opResult.setBody(e.getMessage());
    Throwable t = e.getCause();

    if (t instanceof BizzException) {
      OpResult opResult2 = ((BizzException) e).getOpResult();
      opResult.setCode(opResult2.getCode());
      opResult.setMessage(opResult2.getMessage());
      opResult.setBody(opResult2.getBody());
      opResult.setTotalRecords(opResult2.getTotalRecords());
    } else if (e instanceof NoSuchElementException || e instanceof NullPointerException) {
      opResult.setCode(OpResult.CODE_COMM_NULL_EXCEPTION);
    }

    LOG.error("BaseController Exception: " + e.getClass().getSimpleName(), e);
  }

  protected OpResult handlePermission(OpResult opResult, Long memberId, Long orgId, Short orgType, String authorities, String... authCode) {
    LOG.trace("has authorities: {}", authorities);
    LOG.trace("require authCodes: {}", Arrays.asList(authCode));
    if (StringUtils.isBlank(authorities)) {
      LOG.debug("has NO authorities");
      opResult.setCode(OpResult.CODE_COMM_GRANT_EXPIRED);
      return opResult;
    }

    for (int i = 0; i < authCode.length; i++) {
      if (authorities.contains(authCode[i])) {
        LOG.trace("matched auth code for current method: {}", authCode[i]);
        opResult.setBody(memberId);
        opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
        return opResult;
      }
    }

    opResult.setCode(OpResult.CODE_COMM_GRANT_NOT_ENOUGH);

    return opResult;
  }

  protected boolean isHq(Long orgId) {
    if (orgId == null)
      return false;

    return orgId.longValue() == CommonValues.NUM_ID_HQ;
  }

  protected void throwBizzException(int errorCode) {
    throw new BizzException(errorCode);
  }
}
