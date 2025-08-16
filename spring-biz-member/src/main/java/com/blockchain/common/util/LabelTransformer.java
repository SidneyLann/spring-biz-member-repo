package com.blockchain.common.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LabelTransformer {

  public static void main0(String[] args) {
    String inFolder = args.length > 0 ? args[0] : "c:\\labels3";
    String outFolder = args.length > 1 ? args[1] : "c:\\labels4";

    try {
      Path inPath = Paths.get(inFolder);
      Files.list(inPath).forEach(path -> {
        List<String> inLines;
        List<String> outLines;
        try {
          outLines = new ArrayList<>();
          inLines = Files.readAllLines(path);

          for (String line : inLines) {
            String[] lineArr = line.split(" ");
            StringBuilder sb = new StringBuilder();
            double d = 1.0 - Double.valueOf(lineArr[1]);
            int x = (int) (d * 100000000);
            d = x / 100000000.0;
            sb.append(lineArr[0]);
            sb.append(" ");
            sb.append(lineArr[2]);
            sb.append(" ");
            sb.append(d);
            sb.append(" ");
            sb.append(lineArr[4]);
            sb.append(" ");
            sb.append(lineArr[3]);

            outLines.add(sb.toString());
          }

          String fileName = path.toString();
          fileName = "/4" + fileName.substring(fileName.lastIndexOf("\\") + 2);

          Files.write(Paths.get(outFolder + fileName), outLines);
        } catch (Exception ie) {
          ie.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main2(String[] args) {
    String inFolder = args.length > 0 ? args[0] : "D:\\pcng_piano\\train_piano\\labels\\val";
    String outFolder = args.length > 1 ? args[1] : "c:\\labels4\\";

    try {
      Path inPath = Paths.get(inFolder);
      Files.list(inPath).forEach(path -> {
        List<String> inLines;
        List<String> outLines;
        try {
          outLines = new ArrayList<>();
          inLines = Files.readAllLines(path);

          for (String line : inLines) {
            String[] lineArr = line.split(" ");
            StringBuilder sb = new StringBuilder();
            int label = Integer.parseInt(lineArr[0]);
            if (label == 89)
              label = 90;
            else if (label == 90)
              label = 89;
            else if (label == 93)
              label = 94;
            else if (label == 94)
              label = 93;

            sb.append(label);
            sb.append(" ");
            sb.append(lineArr[1]);
            sb.append(" ");
            sb.append(lineArr[2]);
            sb.append(" ");
            sb.append(lineArr[3]);
            sb.append(" ");
            sb.append(lineArr[4]);

            outLines.add(sb.toString());
          }

          String fileName = path.toString();
          fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

          Files.write(Paths.get(outFolder + fileName), outLines);
        } catch (Exception ie) {
          ie.printStackTrace();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String inFolder = args.length > 0 ? args[0] : "D:\\Sidney\\dev\\ai\\atrain\\boxLabel\\Labels";
    String outFolder = args.length > 1 ? args[1] : "c:\\labels4\\";

    Path inPath = Paths.get(inFolder);
    try {
      countLabels(inPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void countLabels(Path dir) throws IOException {
    Set<String> labels = new HashSet<>();
    Files.list(dir).forEach(path -> {
      try {
        Files.list(path).forEach(path2 -> {
          try {
            List<String> inLines = Files.readAllLines(path2, Charset.forName("GBK"));
            for (String line : inLines) {
              if (line.length() < 10)
                continue;

              String[] lineArr = line.split(" ");
              labels.add(lineArr[4]);
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    System.out.println(labels);
    System.out.println(labels.size());
  }

  private static void countLabels2(Path dir) throws IOException {
    Set<String> labels = new HashSet<>();
    // Files.list(dir).forEach(path -> {
    try {
      Files.list(dir).forEach(path2 -> {
        try {
          List<String> inLines = Files.readAllLines(path2, Charset.forName("GBK"));
          for (String line : inLines) {
            if (line.length() < 10)
              continue;

            String[] lineArr = line.split(" ");
            if (lineArr.length == 5)
              labels.add(lineArr[0]);
            else
              System.out.println(line);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    // });

    System.out.println(labels);
    System.out.println(labels.size());
  }
}
