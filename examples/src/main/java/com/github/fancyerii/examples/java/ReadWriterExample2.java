package com.github.fancyerii.examples.java;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadWriterExample2 {

  public static void main(String[] args) throws Exception {
    String html = "<html>\n<body>Hello Html!</body>\n</html>";
    File tempFile = File.createTempFile("test-", ".tmp");
    System.out.println(tempFile.toURI());
    Files.write(Paths.get(tempFile.toURI()), html.getBytes(StandardCharsets.UTF_8));
    List<String> lines = Files.readAllLines(Paths.get(tempFile.toURI()));
    for (String line : lines) {
      System.out.println(line);
    }
    tempFile.delete();
  }

}
