package com.blockchain.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
  private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);
  private static final String COMMAND_TAR = "tar cvzf - $1 | split --bytes=50M - $2.tar.gz.";
  private static final String[] COMMAND_SHELL = new String[] { "/bin/sh", "-c", "shell command" };

  public static void tarFile(String srcFileStr, String tgtFileStr) throws Exception {
    try {
      COMMAND_SHELL[2] = COMMAND_TAR.replace("$1", srcFileStr).replace("$2", tgtFileStr);
      ProcessBuilder processBuilder = new ProcessBuilder(COMMAND_SHELL);
      processBuilder.redirectErrorStream(true);
      Process process = processBuilder.start();
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String s = null;
      while ((s = stdInput.readLine()) != null) {
        LOG.trace("tarFile: {}", s);
      }
      process.destroy();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void zipFile(String srcFileStr, String destZipFile) throws Exception {
    try (FileOutputStream fileWriter = new FileOutputStream(destZipFile); ZipOutputStream zip = new ZipOutputStream(fileWriter)) {
      File srcFile = new File(srcFileStr);

      if (srcFile.isDirectory()) {
        addFolderToZip(srcFile, zip);
      } else {
        addFileToZip(srcFile, zip);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void addFileToZip(File srcFile, ZipOutputStream zip) throws Exception {
    byte[] buf = new byte[1024];
    int len;
    try (FileInputStream in = new FileInputStream(srcFile)) {
      zip.putNextEntry(new ZipEntry(srcFile.getAbsolutePath()));

      while ((len = in.read(buf)) > 0) {
        zip.write(buf, 0, len);
      }

      zip.flush();
    }
  }

  private static void addFolderToZip(File srcFolder, ZipOutputStream zip) throws Exception {
    for (File file : srcFolder.listFiles()) {
      if (file.isDirectory()) {
        addFolderToZip(file, zip);
      } else {
        addFileToZip(file, zip);
      }
    }
  }

  public static void delete(File file) {
    if (file.isFile()) {
      if (file.exists())
        file.delete();

      return;
    }

    deleteFolder(file);
  }

  private static void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isFile()) {
        files[i].delete();
      } else {
        deleteFolder(files[i]);
      }
    }

    folder.delete();
  }

  public static void mkDir(Path dir) throws IOException {
    if (!Files.exists(dir)) {
      Files.createDirectories(dir);
    }
  }

  public static void rmDir(Path dir) throws IOException {
    if (!Files.exists(dir))
      return;

    Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  public static void mvFile(Path srcPath, Path dstPath) throws IOException {
    if (Files.exists(srcPath)) {
      if (!Files.exists(dstPath))
        Files.createDirectories(dstPath);

      Files.move(srcPath, dstPath);
    }
  }

  public static void mvDir(Path srcPath, Path dstPath, boolean isDelSrc) throws IOException {
    if (Files.exists(srcPath)) {
      Files.move(srcPath, dstPath);
    }
  }

  public static List<String> readAllLines(String pathStr) throws IOException {
    Path srcPath = Path.of(pathStr);
    return Files.readAllLines(srcPath);
  }

  public static String fileToString(String pathStr) throws IOException {
    Path srcPath = Path.of(pathStr);
    return Files.readString(srcPath);
  }

  public static List<String> readFilesAsOneString(String dir) throws IOException {
    Path folderPath = Path.of(dir);
    final List<String> output = new ArrayList<>();

    try (Stream<Path> paths = Files.list(folderPath)) {
      paths.filter(Files::isRegularFile)
          .forEach(filePath -> {
            try {
              String content = Files.readString(filePath);
              output.add(content);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output;
  }

  public static String readFileAsOneString(String pathStr) throws IOException {
    Path path = Path.of(pathStr);
    
    return Files.readString(path);
  }

  public static void main(String[] args) {
    String root = "D:\\Sidney\\股票";
    try {
      Path src = Paths.get("D:\\tmp\\1");
      Path tgt = Paths.get("D:\\1");
      mvDir(src, tgt, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
