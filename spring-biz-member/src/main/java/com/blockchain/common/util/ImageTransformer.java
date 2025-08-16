package com.blockchain.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ImageTransformer {
//  private static String SCALE_JPG = "480:480";
//  private static String SCALE_PNG = "480:480";

  private static String SCALE_JPG = "640:640";
  private static String SCALE_PNG = "300:400";

  public static void main(String[] args) {
    String inFolder = args.length > 0 ? args[0] : "D:\\pcng_eb\\商城图片\\服务\\线上服务\\钢琴培训";
    String outFolder = args.length > 1 ? args[1] : "c:\\image";
    try {
      Path path = Paths.get(inFolder);

      //getOldIdea(path);

      File file = path.toFile();
      handleFile(file, inFolder, outFolder);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void handleFile(File file, String inFolder, String outFolder) throws IOException {
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File file2 : files) {
        String directoryOutStr = file.getAbsolutePath();
        directoryOutStr = directoryOutStr.replace(inFolder, outFolder);
        File directoryOutFile = new File(directoryOutStr);
        if (!directoryOutFile.exists()) {
          directoryOutFile.mkdir();
        }
        handleFile(file2, inFolder, outFolder);
      }
    } else {
      // System.out.println(file.getName());
      String transpose = " -vf transpose=2";

//      BufferedImage bufferedImage = ImageIO.read(file);
//      String fileName = file.getName().toLowerCase();
//      if (bufferedImage != null) {
//        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")) {
//          int width = bufferedImage.getWidth();
//          int height = bufferedImage.getHeight();
//          if (width > height)
//            transpose = " -vf transpose=2";
//        }
//      }

      String fileNameIn = file.getAbsolutePath();
      String fileNameOut = fileNameIn.replace(inFolder, outFolder);
      String command = "D:\\Program Files\\ffmpeg6\\bin\\ffmpeg.exe -i " + fileNameIn + " -vf scale=" + (fileNameIn.endsWith("png") ? SCALE_PNG : SCALE_JPG) + " " + fileNameOut;
      //String command = "D:\\Program Files\\ffmpeg6\\bin\\ffmpeg.exe -i " + fileNameIn +  + " " + fileNameOut;

      System.out.println(command);
      new ProcessBuilder(command.split(" ")).start();
    }
  }

  private static void renameFile(Path dir) throws IOException {
    Files.list(dir).forEach(path -> {
      try {
        String fileName = path.toString();
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        if (fileName.lastIndexOf("_") == 1)
          fileName = "4_" + fileName;
        else if (fileName.lastIndexOf("_") == 3)
          return;
        else
          fileName = "4_1_" + fileName;

        Files.move(path, path.resolveSibling(fileName));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private static void renameFile2(Path dir) throws IOException {
    Files.list(dir).forEach(path -> {
      try {
        String fileName = path.toString();
        fileName = "4" + fileName.substring(fileName.lastIndexOf("\\") + 2);

        Files.move(path, Paths.get("C:\\images5\\" + fileName));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private static void renameFile3(Path dir) throws IOException {
    Files.list(dir).forEach(path -> {
      try {
        Files.list(path).forEach(path2 -> {

          String fileName = path2.toString();
          fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
          try {
            Files.move(path2, Paths.get("C:\\images\\1_" + fileName));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private static void renameFile4(Path dir) throws IOException {
    Files.list(dir).forEach(path -> {
      try {
        String fileName = path.toString();
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

        List<String> lines = Files.readAllLines(path);
        List<String> newLines = new ArrayList<>();

        for (String line : lines) {
          String[] elements = line.split(" ");
          newLines.add(elements[0] + " " + elements[1] + " " + elements[2] + " " + elements[3] + " " + elements[4]);
        }

        Files.write(Paths.get("C:\\labels\\" + fileName), newLines);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private static void renameFile5(Path dir) throws IOException {
    final List<Integer> labels = new ArrayList<>();
    final List<Path> ramdomLabels = new ArrayList<>();
    Iterator<Path> it = Files.list(dir).iterator();
    while (it.hasNext()) {
      ramdomLabels.add(it.next());
    }
    Collections.shuffle(ramdomLabels);
    System.out.println(ramdomLabels.size());
    try {
      for (Path path : ramdomLabels) {
        List<String> inLines = Files.readAllLines(path);
        for (String line : inLines) {
          if (line.length() < 10)
            continue;

          String[] lineArr = line.split(" ");
          int labelIdx = Integer.parseInt(lineArr[0]);

          if (labels.contains(labelIdx)) {
            continue;
          } else {
            labels.add(labelIdx);
            String fileName = path.toString();
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            // Files.move(path, Paths.get("C:\\labels\\val\\" + fileName));
            // Files.move(Paths.get("C:\\images\\train\\" + fileName.replace("txt", "jpg")), Paths.get("C:\\images\\val\\" + fileName.replace("txt", "jpg")));

            break;
          }
        }

        if (labels.size() == 96)
          break;
      }
      Collections.sort(labels);
      System.out.println(labels);
      System.out.println(labels.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void renameFile6(Path dir) throws IOException {
    Files.list(dir).forEach(path -> {
      try {
        String labelFileName = path.toString().replace("images", "labels").replace("jpg", "txt");
        String fileName = path.toString();
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

        if (!Files.exists(Paths.get(labelFileName)))
          Files.move(path, Paths.get("C:\\images5\\" + fileName));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private static void getOldIdea(Path dir) throws IOException {
    List<String> inLines = Files.readAllLines(Paths.get("C:\\20241009.txt"));
    Files.list(dir).forEach(path -> {
      try {
        List<String> inLines2 = Files.readAllLines(path);
        for (String idea : inLines) {
          for (String idea2 : inLines2) {
            if (idea.trim().equals(idea2.trim()))
              System.out.println(idea);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
