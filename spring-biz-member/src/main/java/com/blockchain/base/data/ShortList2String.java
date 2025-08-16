package com.blockchain.base.data;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;

public class ShortList2String implements AttributeConverter<List<Short>, String> {
  @Override
  public String convertToDatabaseColumn(List<Short> ids) {
    if (ids == null)
      return null;

    return ids.toString().replace("[", "").replace("]", "");
  }

  @Override
  public List<Short> convertToEntityAttribute(String s) {
    if (StringUtils.isBlank(s))
      return null;

    String[] strArr = s.split(",");
    List<Short> ids = new ArrayList<>(strArr.length);
    for (String id : strArr)
      ids.add(Short.valueOf(id.trim()));

    return ids;
  }
}
