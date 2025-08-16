package com.blockchain.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
  public static final String TEMPLATE_HOME = "/home/sidney/app/data/excel/";

  public static final String copyFile(String templateName) throws Exception {
    String workbook = null;
    try {
      long fileNo = System.currentTimeMillis();
      workbook = templateName.replace(".xlsx", fileNo + ".xlsx");
      File templateFile = new File(TEMPLATE_HOME + templateName);
      File templateFile2 = new File(TEMPLATE_HOME + workbook);
      Files.copy(templateFile.toPath(), templateFile2.toPath());
    } catch (Exception e) {
      e.printStackTrace();

      throw e;
    } finally {
    }

    return workbook;
  }

  public static final Sheet getSheet(String templateName, String sheetName) throws Exception {
    Workbook workbook = null;
    try {
      File templateFile = new File(TEMPLATE_HOME + templateName);
      workbook = new XSSFWorkbook(templateFile);

      if (StringUtils.isNotBlank(sheetName)) {
        workbook.setSheetName(0, sheetName);
      }

      Sheet sheet = workbook.getSheetAt(0);
      return sheet;
    } catch (Exception e) {
      e.printStackTrace();

      if (workbook != null)
        workbook.close();

      throw e;
    } finally {
//      if (workbook != null)
//        workbook.close();
    }
  }

  public static final byte[] printWorkboot(Workbook workbook) throws Exception {
    ByteArrayOutputStream os = null;
    try {
      os = new ByteArrayOutputStream();
      workbook.write(os);
      return os.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (workbook != null) {
        workbook.close();
      }

      if (os != null)
        os.close();
    }
  }
}
