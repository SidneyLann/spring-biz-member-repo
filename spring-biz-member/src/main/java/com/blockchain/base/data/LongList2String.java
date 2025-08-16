package com.blockchain.base.data;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeConverter;

import org.apache.commons.lang3.StringUtils;

public class LongList2String implements AttributeConverter<List<Long>, String> {
  @Override
  public String convertToDatabaseColumn(List<Long> ids) {
    if (ids == null)
      return null;

    return ids.toString().replace("[", "").replace("]", "");
  }

  @Override
  public List<Long> convertToEntityAttribute(String s) {
    if (StringUtils.isBlank(s))
      return null;

    String[] strArr = s.split(",");
    List<Long> ids = new ArrayList<>(strArr.length);
    for (String id : strArr)
      ids.add(Long.valueOf(id.trim()));

    return ids;
  }
}
