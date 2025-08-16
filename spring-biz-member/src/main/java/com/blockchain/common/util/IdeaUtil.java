package com.blockchain.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IdeaUtil {
  public static void main(String[] args) {
    String inFolder = args.length > 0 ? args[0] : "D:\\pcng_eb\\商城图片\\服务\\线上服务\\钢琴培训";
    String outFolder = args.length > 1 ? args[1] : "c:\\image";
    try {
      Path path = Paths.get(inFolder);

      getOldIdea();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void getOldIdea() throws IOException {
    Path dir = Paths.get("C:\\new");

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
