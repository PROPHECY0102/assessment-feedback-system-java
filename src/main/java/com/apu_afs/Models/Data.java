package com.apu_afs.Models;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Data {
  public static List<String> fetch(String filepathStr) {
    Path filePath = Paths.get(filepathStr);

    try {
      String data = Files.readString(filePath);
      if (data.isEmpty()) {
        return new ArrayList<String>();
      }

      List<String> rows = new ArrayList<String>(Arrays.asList(data.split("\n")));
      return rows;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new ArrayList<String>();
  }

  public static void save(String filepathStr, String content) {
    Path filePath = Paths.get(filepathStr);

    try {
      Files.writeString(filePath, content, StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
