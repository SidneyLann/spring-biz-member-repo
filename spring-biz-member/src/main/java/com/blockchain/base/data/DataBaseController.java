package com.blockchain.base.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import com.blockchain.base.BaseController;
import com.blockchain.common.base.BizzException;
import com.blockchain.common.base.OpResult;
import com.blockchain.common.constants.CacheKeys;

import feign.FeignException;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DataBaseController extends BaseController {
  private static final Logger LOG = LoggerFactory.getLogger(DataBaseController.class);
  private static final String IGNORE_FIELDS = "id,remark,createBy,updateBy,mallType";
  @Resource
  protected IdGenerator idGenerator;
  @Resource
  protected CacheClient cacheClient;
  @Resource
  protected UserIDAuditor userIDAuditor;

  protected Object genFieldJs(Class<?> clazz, int apiType) {
    Object result = "";
    if (apiType == 1)
      result = genFieldJsFn(clazz);
    else if (apiType == 3)
      result = genFieldJsNoFn(clazz);

    return result;
  }

  protected Object genFormJs(Class<?> clazz, int apiType) {
    Object result = "";
    if (apiType == 1)
      result = genFormJs4State(clazz);
    else if (apiType == 3)
      result = genFormJs4State(clazz);

    return result;
  }

  protected String genFieldJsFn(Class<?> clazz) {
    StringBuilder validateJss = new StringBuilder();
    Field[] fields1 = clazz.getDeclaredFields();
    Field[] fields2 = clazz.getSuperclass().getDeclaredFields();
    Field[] fields = new Field[fields1.length + fields2.length];
    // Field[] fields = clazz.getDeclaredFields();
    System.arraycopy(fields1, 0, fields, 0, fields1.length);
    System.arraycopy(fields2, 0, fields, fields1.length, fields2.length);
    Annotation[] annotations = null;
    Class<?> annClazz = null;
    String fieldName = null;
    boolean isFirstField = true;
    boolean isFirstAnnot = true;
    validateJss.append("let fieldName = event.target.id;let fieldValue = event.target.value;");

    for (Field field : fields) {
      fieldName = field.getName();
      LOG.trace("fieldName: {}", fieldName);

      if (IGNORE_FIELDS.contains(fieldName))
        continue;

      annotations = field.getAnnotations();

      boolean hasValidAnn = false;
      for (Annotation annotation : annotations) {
        String annClassName = annotation.annotationType().getTypeName();
        LOG.trace("annClassName: {}", annClassName);
        if (annClassName.contains("jakarta.validation.constraints") || annClassName.contains("org.hibernate.validator.constraints")) {
          hasValidAnn = true;
          break;
        }
      }
      if (hasValidAnn) {
        if (isFirstField) {
          isFirstField = false;
        } else {
          validateJss.append("else");
        }
        validateJss.append(" if('" + fieldName + "'==fieldName){");
      }

      for (Annotation annotation : annotations) {
        annClazz = annotation.annotationType();
        if (annClazz == Email.class) {

        } else if (annClazz == Size.class) {
          LOG.trace("Length annotation: {}", annClazz.getName());
          Size length = (Size) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append(" if(!validator.isLength(fieldValue,{min:" + length.min() + ",max:" + length.max() + "})){this.labels[fieldName]='字符数必须介于" + length.min() + "和" + length.max()
              + "之间';this.state.errors[fieldName]=true;}");
        } else if (annClazz == NotEmpty.class) {
          LOG.trace("NotEmpty annotation: {}", annClazz.getName());
          NotEmpty notEmpty = (NotEmpty) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append("if(validator.isEmpty(fieldValue)){this.labels[fieldName]='字段值不能为空';this.state.errors[fieldName]=true;}");
        } else if (annClazz == NotBlank.class) {
          LOG.trace("NotBlank annotation: {}", annClazz.getName());
          NotBlank notBlank = (NotBlank) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append("if(validator.isEmpty(fieldValue)){this.labels[fieldName]='字段值不能为空';this.state.errors[fieldName]=true;}");
        } else if (annClazz == NotNull.class) {
          LOG.trace("NotBlank annotation: {}", annClazz.getName());
          NotNull notBlank = (NotNull) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append("if(validator.isEmpty(fieldValue)){this.labels[fieldName]='字段值不能为空';this.state.errors[fieldName]=true;}");
        } else if (annClazz == Min.class) {
          LOG.trace("Min annotation: {}", annClazz.getName());
          Min min = (Min) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append(" if(!validator.isFloat(fieldValue,{min:" + min.value() + "})){this.labels[fieldName]='数值不能小于" + min.value() + "';this.state.errors[fieldName]=true;}");
        } else if (annClazz == Max.class) {
          LOG.trace("Max annotation: {}", annClazz.getName());
          Max max = (Max) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append(" if(!validator.isFloat(fieldValue,{max:" + max.value() + "})){this.labels[fieldName]='数值不能大于" + max.value() + "';this.state.errors[fieldName]=true;}");
        } else if (annClazz == Range.class) {
          LOG.trace("Range annotation: {}", annClazz.getName());
          Range range = (Range) annotation;
          long min = range.min();
          long max = range.max();
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append(" if(!validator.isRange(fieldValue,{min:" + range.min() + ",max:" + range.max() + "})){this.labels[fieldName]='字符数必须介于" + range.min() + "和" + range.max()
              + "之间';this.state.errors[fieldName]=true;}");

        } else if (annClazz == Digits.class) {
          LOG.trace("Digits annotation: {}", annClazz.getName());
          Digits digits = (Digits) annotation;
          long integer = digits.integer();
          long fraction = digits.fraction();
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append(" if(fieldValue.lastIndexOf('.')>0&& fieldValue.substring(fieldValue.lastIndexOf('.')+1).length>" + fraction + ") {this.labels[fieldName]='小数位不能大于" + fraction
              + "';this.state.errors[fieldName]=true;}");
        } else if (annClazz == Email.class) {
          LOG.trace("URL annotation: {}", annClazz.getName());
          Email email = (Email) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append("if(fieldValue!='' && !validator.isEmail(fieldValue)){this.labels[fieldName]='字段值不是合法的邮件地址';this.state.errors[fieldName]=true;}");
        } else if (annClazz == URL.class) {
          LOG.trace("URL annotation: {}", annClazz.getName());
          URL url = (URL) annotation;
          if (isFirstAnnot) {
            isFirstAnnot = false;
          } else {
            validateJss.append("else");
          }
          validateJss.append("if(fieldValue!='' && !validator.isURL(fieldValue)){this.labels[fieldName]='字段值不是合法的网址';this.state.errors[fieldName]=true;}");
        } else {
          LOG.trace("not validate annotation: {}", annClazz.getName());
        }
      }

      if (hasValidAnn) {
        validateJss.append("else{this.labels[fieldName]=this.labels0[fieldName]; this.state.errors[fieldName]=false;}");
        validateJss.append("}");
      }

      isFirstAnnot = true;
    }

    return validateJss.toString();
  }

  protected Map<String, Map<String, Object>> genFieldJsNoFn(Class<?> clazz) {// not depend on client js validator function
    Map<String, Map<String, Object>> validateJson = new HashMap<>();
    Map<String, Object> typeMessages = null;
    Field[] fields1 = clazz.getDeclaredFields();
    Field[] fields2 = clazz.getSuperclass().getDeclaredFields();
    Field[] fields = new Field[fields1.length + fields2.length];
    // Field[] fields = clazz.getDeclaredFields();
    System.arraycopy(fields1, 0, fields, 0, fields1.length);
    System.arraycopy(fields2, 0, fields, fields1.length, fields2.length);
    Annotation[] annotations = null;
    Class<?> annClazz = null;
    String fieldName = null;

    for (Field field : fields) {
      fieldName = field.getName();
      LOG.trace("fieldName: {}", fieldName);

      if (IGNORE_FIELDS.contains(fieldName))
        continue;

      annotations = field.getAnnotations();

      boolean hasValidAnn = false;
      for (Annotation annotation : annotations) {
        String annClassName = annotation.annotationType().getTypeName();
        LOG.trace("annClassName: {}", annClassName);
        if (annClassName.contains("jakarta.validation.constraints") || annClassName.contains("org.hibernate.validator.constraints")) {
          hasValidAnn = true;
          break;
        }
      }

      if (!hasValidAnn) {
        continue;
      }

      typeMessages = new HashMap<>();
      for (Annotation annotation : annotations) {
        annClazz = annotation.annotationType();
        if (annClazz == Email.class) {

        } else if (annClazz == Size.class) {
          LOG.trace("Length annotation: {}", annClazz.getName());
          Size length = (Size) annotation;

          typeMessages.put("Size", "字符数必须介于" + length.min() + "和" + length.max() + "之间");
          typeMessages.put("Size1", length.min());
          typeMessages.put("Size2", length.max());
        } else if (annClazz == NotEmpty.class) {
          LOG.trace("NotEmpty annotation: {}", annClazz.getName());
          NotEmpty notEmpty = (NotEmpty) annotation;

          typeMessages.put("NotEmpty", "字段值不能为空");
        } else if (annClazz == NotBlank.class) {
          LOG.trace("NotBlank annotation: {}", annClazz.getName());
          NotBlank notBlank = (NotBlank) annotation;

          typeMessages.put("NotBlank", "字段值不能为空");
        } else if (annClazz == NotNull.class) {
          LOG.trace("NotNull annotation: {}", annClazz.getName());

          typeMessages.put("NotNull", "字段值不能为空");
        } else if (annClazz == Min.class) {
          LOG.trace("Min annotation: {}", annClazz.getName());
          Min min = (Min) annotation;

          typeMessages.put("Min", "数值不能小于" + min.value());
          typeMessages.put("Min1", min.value());
        } else if (annClazz == Max.class) {
          LOG.trace("Max annotation: {}", annClazz.getName());
          Max max = (Max) annotation;

          typeMessages.put("Max", "数值不能大于" + max.value());
          typeMessages.put("Max1", max.value());
        } else if (annClazz == Range.class) {
          LOG.trace("Range annotation: {}", annClazz.getName());
          Range range = (Range) annotation;
          long min = range.min();
          long max = range.max();

          typeMessages.put("Range", "字符数必须介于" + min + "和" + max + "之间");
          typeMessages.put("Range1", min);
          typeMessages.put("Range2", max);
        } else if (annClazz == Digits.class) {
          LOG.trace("Digits annotation: {}", annClazz.getName());
          Digits digits = (Digits) annotation;
          long integer = digits.integer();
          long fraction = digits.fraction();

          typeMessages.put("Digits", "小数位不能大于" + fraction);
          typeMessages.put("Digits1", integer);
          typeMessages.put("Digits2", fraction);
        } else if (annClazz == Email.class) {
          LOG.trace("URL annotation: {}", annClazz.getName());
          Email email = (Email) annotation;

          typeMessages.put("Email", "字段值不是合法的邮件地址");
        } else if (annClazz == URL.class) {
          LOG.trace("URL annotation: {}", annClazz.getName());
          URL url = (URL) annotation;

          typeMessages.put("URL", "字段值不是合法的网址");
        } else {
          LOG.trace("not validate annotation: {}", annClazz.getName());
        }
      }

      validateJson.put(fieldName, typeMessages);
    }

    return validateJson;
  }

  protected String genFormJs4State(Class<?> clazz) {
    StringBuilder validateJss = new StringBuilder();
    Field[] fields1 = clazz.getDeclaredFields();
    Field[] fields2 = clazz.getSuperclass().getDeclaredFields();
    Field[] fields = new Field[fields1.length + fields2.length];
    // Field[] fields = clazz.getDeclaredFields();
    System.arraycopy(fields1, 0, fields, 0, fields1.length);
    System.arraycopy(fields2, 0, fields, fields1.length, fields2.length);
    Annotation[] annotations = null;
    Class<?> annClazz = null;
    String fieldName = null;
    validateJss.append("let fieldName = '';let fieldValue = '';let msgs=''; ");
    for (Field field : fields) {
      fieldName = field.getName();
      LOG.trace("fieldName: {}", fieldName);

      if (IGNORE_FIELDS.contains(fieldName))
        continue;

      annotations = field.getAnnotations();

      boolean hasValidAnn = false;
      for (Annotation annotation : annotations) {
        String annClassName = annotation.annotationType().getTypeName();
        LOG.trace("annClassName: {}", annClassName);
        if (annClassName.contains("jakarta.validation.constraints") || annClassName.contains("org.hibernate.validator.constraints")) {
          hasValidAnn = true;
          break;
        }
      }
      if (hasValidAnn) {
        validateJss.append(" fieldName='" + fieldName + "';");
        validateJss.append(" fieldValue=params[fieldName]; if(fieldValue==undefined||fieldValue==null) fieldValue=''; fieldValue=''+fieldValue; ");
        validateJss.append(" if(this.labels0[fieldName]) fieldName=this.labels0[fieldName];");
      }

      for (Annotation annotation : annotations) {
        annClazz = annotation.annotationType();
        if (annClazz == Email.class) {

        } else if (annClazz == Size.class) {
          LOG.trace("Length annotation: {}", annClazz.getName());
          Size length = (Size) annotation;
          validateJss.append(" if(!validator.isLength(fieldValue,{min:" + length.min() + ",max:" + length.max() + "})){msgs+=fieldName+': 字符数必须介于" + length.min() + "和" + length.max() + "之间\\n';} ");
        } else if (annClazz == NotEmpty.class) {
          LOG.trace("NotEmpty annotation: {}", annClazz.getName());
          NotEmpty notEmpty = (NotEmpty) annotation;
          validateJss.append("if(validator.isEmpty(fieldValue)){msgs+=fieldName+': 字段值不能为空\\n';} ");
        } else if (annClazz == NotBlank.class) {
          LOG.trace("NotBlank annotation: {}", annClazz.getName());
          NotBlank notBlank = (NotBlank) annotation;
          validateJss.append("if(validator.isEmpty(fieldValue)){msgs+=fieldName+': 字段值不能为空\\n';} ");
        } else if (annClazz == NotNull.class) {
          LOG.trace("NotNull annotation: {}", annClazz.getName());
          NotNull notNull = (NotNull) annotation;
          validateJss.append("if(validator.isEmpty(fieldValue)){msgs+=fieldName+': 字段值不能为空\\n';} ");
        } else if (annClazz == Min.class) {
          LOG.trace("Range annotation: {}", annClazz.getName());
          Min min = (Min) annotation;
          validateJss.append(" if(!validator.isFloat(fieldValue,{min:" + min.value() + "})){msgs+=fieldName+': 数值不能小于" + min.value() + "\\n';} ");
        } else if (annClazz == Max.class) {
          LOG.trace("Range annotation: {}", annClazz.getName());
          Max max = (Max) annotation;
          validateJss.append(" if(!validator.isFloat(fieldValue,{max:" + max.value() + "})){msgs+=fieldName+': 数值不能大于" + max.value() + "\\n';} ");
        } else if (annClazz == Range.class) {
          LOG.trace("Range annotation: {}", annClazz.getName());
          Range range = (Range) annotation;
          validateJss.append(" if(!validator.isRange(fieldValue,{min:" + range.min() + ",max:" + range.max() + "})){msgs+=fieldName+': 字符数必须介于" + range.min() + "和" + range.max() + "之间\\n';} ");
        } else if (annClazz == Digits.class) {
          LOG.trace("Digits annotation: {}", annClazz.getName());
          Digits digits = (Digits) annotation;
          long integer = digits.integer();
          long fraction = digits.fraction();
          validateJss.append(" if(fieldValue.lastIndexOf('.')>0&& fieldValue.substring(fieldValue.lastIndexOf('.')+1).length>" + fraction + ") {msgs+=fieldName+': 小数位不能大于 " + fraction + "\\n';} ");
        } else if (annClazz == Email.class) {
          LOG.trace("URL annotation: {}", annClazz.getName());
          Email email = (Email) annotation;
          validateJss.append("if(fieldValue!='' && !validator.isEmail(fieldValue)){msgs+=fieldName+': 字段值不是合法的邮件地址\\n'; }");
        } else if (annClazz == URL.class) {
          LOG.trace("URL annotation: {}", annClazz.getName());
          URL url = (URL) annotation;
          validateJss.append("if(fieldValue!='' && !validator.isURL(fieldValue)){msgs+=fieldName+': 字段值不是合法的网址\\n';} ");
        } else if (fieldName.startsWith("idCardNo")) {
          validateJss.append("if(fieldValue!='' && !JsUtil.idcardFormat(fieldValue)){msgs+=fieldName+': 字段值不是合格的身份证号码\\n';} ");
        } else if (fieldName.endsWith("Phone")) {
          validateJss.append("if(fieldValue!='' && !JsUtil.phoneFormat(fieldValue)){msgs+=fieldName+': 字段值不是合格的电话号码\\n';} ");
        } else {
          LOG.trace("not checking annotation: {}", annClazz.getName());
        }
      }
    }

    validateJss.append(" return msgs;");

    return validateJss.toString();
  }

  protected List<String> handleViolations(Iterator<ConstraintViolation<?>> violats) {
    ConstraintViolation<?> cv = null;
    List<String> msgList = new ArrayList<>();
    while (violats.hasNext()) {
      cv = violats.next();
      String entityName = cv.getRootBeanClass().getSimpleName();
      String fieldName = cv.getPropertyPath().toString();
      String message = cv.getMessage();
      if (message.indexOf("null") != -1) {
        message = "字段值不能为空";
      } else if (message.indexOf("blank") != -1) {
        message = "字段值不能为空";
      } else if (message.indexOf("between") != -1) {
        message = "字符数应在" + message.substring(message.indexOf("between") + 7).replace("and", "和") + "之间";
      } else if (message.indexOf("greater") != -1) {
        message = "数值必须大于或等于" + message.substring(message.lastIndexOf(" "));
      } else if (message.indexOf("less than") != -1) {
        message = "数值必须小或等于" + message.substring(message.lastIndexOf(" "));
      }

      LOG.trace("entityName=" + entityName);
      msgList.add(fieldName);
      msgList.add(message);
    }

    return msgList;
  }

  protected void handleTransactionSystemException(TransactionSystemException e, OpResult opResult) {
    opResult.setCode(OpResult.CODE_COMM_INPUT_ERROR);
    opResult.setBody(e.getMessage());

    Throwable throwable = e.getRootCause();
    String errorMsg = throwable.getMessage();
    if (throwable instanceof ConstraintViolationException) {
      opResult.setBody(handleViolations(((ConstraintViolationException) throwable).getConstraintViolations().iterator()));
    } else if (errorMsg.startsWith("Duplicate entry")) {
      opResult.setCode(OpResult.CODE_COMM_RECORD_EXISTS);
      opResult.setBody(errorMsg);
    }

    LOG.error("TransactionSystemException class name: " + e.getClass().getSimpleName(), e);
  }

  protected void handleJpaSystemException(Throwable t, OpResult opResult) {
    opResult.setCode(OpResult.CODE_COMM_INPUT_ERROR);
    opResult.setBody(t.getMessage());

    String errorMsg = t.getMessage();
    if (t instanceof ConstraintViolationException) {
      opResult.setBody(handleViolations(((ConstraintViolationException) t).getConstraintViolations().iterator()));
    } else if (errorMsg.startsWith("Duplicate entry")) {
      opResult.setCode(OpResult.CODE_COMM_RECORD_EXISTS);
      opResult.setBody(errorMsg);
    }

    LOG.error("Throwable class name: " + t.getClass().getSimpleName(), t);
  }

  @Override
  protected void handleException(Exception e, OpResult opResult) {
    opResult.setCode(OpResult.CODE_COMM_EXCEPTION);
    opResult.setBody(e.getMessage());
    Throwable t = e.getCause();

    if (e instanceof BizzException) {
      OpResult opResult2 = ((BizzException) e).getOpResult();
      opResult.setCode(opResult2.getCode());
      opResult.setMessage(opResult2.getMessage());
      opResult.setBody(opResult2.getBody());
      opResult.setTotalRecords(opResult2.getTotalRecords());
    } else if (e instanceof FeignException) {
      opResult.setMessage(e.getMessage());
    } else if (e instanceof TransactionSystemException) {
      handleTransactionSystemException((TransactionSystemException) e, opResult);
    } else if (e instanceof DataIntegrityViolationException && ((DataIntegrityViolationException) e).getRootCause().getMessage().startsWith("Duplicate entry")) {
      opResult.setCode(OpResult.CODE_COMM_RECORD_EXISTS);
    } else if (e instanceof NoSuchElementException || e instanceof NullPointerException) {
      opResult.setCode(OpResult.CODE_COMM_NULL_EXCEPTION);
    } else if (t instanceof BizzException) {
      OpResult opResult2 = ((BizzException) t).getOpResult();
      opResult.setCode(opResult2.getCode());
      opResult.setMessage(opResult2.getMessage());
      opResult.setBody(opResult2.getBody());
      opResult.setTotalRecords(opResult2.getTotalRecords());
    } else if (t instanceof FeignException) {
      opResult.setMessage(t.getMessage());
    }

    LOG.error("Exception class name: " + e.getClass().getSimpleName(), e);
  }

  @Override
  protected OpResult handlePermission(OpResult opResult, Long memberId, Long orgId, Short orgType, String authorities, String... authCode) {
    LOG.trace("bizz memberId: {}", memberId);
    LOG.trace("bizz orgId: {}", orgId);
    LOG.trace("bizz orgType: {}", orgType);
    LOG.trace("bizz member has authorities: {}", authorities);
    LOG.trace("bizz method required one of authCodes: {}", Arrays.asList(authCode));
    if (memberId == null || orgType == null || orgType < 0 || orgType > 20 || StringUtils.isBlank(authorities)) {
      LOG.warn("memberId == null || orgType == null || orgType < 0 || orgType > 20 || StringUtils.isBlank(authorities)");
      opResult.setCode(OpResult.CODE_COMM_GRANT_EXPIRED);
      return opResult;
    }

    for (int i = 0; i < authCode.length; i++) {
      if (authorities.contains(authCode[i])) {
        LOG.trace("matched auth code for current method: {}", authCode[i]);
        userIDAuditor.setUserId(memberId);
        opResult.setCode(OpResult.CODE_COMM_0_SUCCESS);
        return opResult;
      }
    }

    LOG.warn("OpResult.CODE_COMM_GRANT_NOT_ENOUGH");
    opResult.setCode(OpResult.CODE_COMM_GRANT_NOT_ENOUGH);

    return opResult;
  }

  protected Long getOrgId(Long memberId) {
    if (memberId == null)
      throwBizzException(OpResult.CODE_COMM_GRANT_EXPIRED);

    Long orgId = cacheClient.getLong(CacheKeys.CURR_ORG_ID + memberId);
    if (orgId == null)
      throwBizzException(OpResult.CODE_COMM_GRANT_EXPIRED);

    return orgId;
  }
}
