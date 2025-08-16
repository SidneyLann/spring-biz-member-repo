package com.blockchain.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Component
public class MailUtil {
	  private static final Logger LOG = LoggerFactory.getLogger(MailUtil.class);
  @Value(value = "${spring.mail.from}")
  private String from;
  @Value(value = "${spring.mail.auth.code1}")
  private String code1;
  @Value(value = "${spring.mail.auth.code2}")
  private String code2;
  @Value(value = "${spring.mail.auth.code3}")
  private String code3;
  private Session session;
  @Resource
  private JavaMailSender mailSender;

  @PostConstruct
  public void init() {
    Properties prop = new Properties();
    prop.put("mail.debug", "true");
    prop.put("mail.store.protocol", "imap");
    prop.put("mail.imap.host", "imap.qq.com");
    prop.put("mail.imap.port", 993);
    prop.put("mail.imap.ssl.enable", "true");

    session = Session.getInstance(prop);
  }

  public void sendSampleMail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
  }

  public void sendAttachmentMail(String to, String subject, String content, String attachmentName, String filePath) throws Exception {
    MimeMessagePreparator preparator = new MimeMessagePreparator() {
      @Override
      public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(content);

        File file = new File(filePath);
        if (file.length() > 50 * 1024 * 1024) {
          FileSystemResource bigFile = new FileSystemResource(file);
          messageHelper.addAttachment(attachmentName, bigFile);
        } else {
          messageHelper.addAttachment(attachmentName, file);
        }
      }
    };

    try {
      this.mailSender.send(preparator);
    } catch (Exception e) {
      LOG.error("send email error: {}", e);
    }
  }

  public void sendAttachmentMail4MultiFiles(String to, String subject, String content, String attachmentName, String filePath) throws Exception {
    LOG.debug("send email for folder: {}", filePath);
    File file = new File(filePath);
    String[] fileNames = file.list();
    for (String fileName : fileNames) {
      LOG.trace("fileNames: {}", new Object[] { fileNames });
      if (fileName.startsWith(attachmentName + ".tar.gz.")) {
        LOG.debug("send email for file: {}", filePath + "/" + fileName);
        sendAttachmentMail(to, subject, fileName, fileName, filePath + "/" + fileName);
      }
    }
  }

  public void sendTemplateMail(String to, String subject, String templatePath, String... arguments) throws Exception {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(this.buildTemplateContext(templatePath, arguments), true);
    mailSender.send(mimeMessage);
  }

  private String buildTemplateContext(String templatePath, String... arguments) {
    org.springframework.core.io.Resource resource = new ClassPathResource(templatePath);
    InputStream inputStream = null;
    BufferedReader fileReader = null;
    StringBuffer buffer = new StringBuffer();
    String line = "";
    try {
      inputStream = resource.getInputStream();
      fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
      while ((line = fileReader.readLine()) != null) {
        buffer.append(line);
      }
    } catch (Exception e) {
      LOG.error("读取模板失败:", e);
    } finally {
      if (fileReader != null) {
        try {
          fileReader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return MessageFormat.format(buffer.toString(), arguments);
  }

  public void deleteMails(String account, String folderName, int startIdx, int endIdx, int remainDays) {
    String auth = null;
    try (Store store = session.getStore();) {
      if (account.startsWith("744915225"))
        auth = code1;
      else if (account.startsWith("859622198"))
        auth = code2;
      else if (account.startsWith("744915225"))
        auth = code3;
      else
        return;

      store.connect(account, auth);
      Folder inbox = store.getFolder(folderName);
      inbox.open(Folder.READ_WRITE);
      int totalMessages = inbox.getMessageCount();
      Message[] messages = inbox.getMessages(startIdx, endIdx > totalMessages ? totalMessages : endIdx);
      Date receivedDate;
      Calendar calendar = new GregorianCalendar();
      calendar.add(Calendar.DAY_OF_YEAR, -remainDays);
      for (int i = 0, count = messages.length; i < count; i++) {
        MimeMessage mail = (MimeMessage) messages[i];
        receivedDate = mail.getReceivedDate();
        if (calendar.getTime().after(receivedDate)) {
          mail.setFlag(Flags.Flag.DELETED, true);
        }
      }

      inbox.close();
    } catch (Exception e) {
      LOG.error("delete email error: ", e);
    }
  }

  public static void main(String[] args) {
    try {
      MailUtil mailUtil = new MailUtil();
      mailUtil.deleteMails("507320273@qq.com", "收件箱", 1, 100, 7);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}