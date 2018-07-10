package com.github.fancyerii.examples.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class ReadWriterExample {

  public static void main(String[] args) throws Exception {
    String html = "<html>\n<body>Hello Html!</body>\n</html>";
    File tempFile = File.createTempFile("test-", ".tmp");
    System.out.println(tempFile.getAbsolutePath());
    try (BufferedWriter bw = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8));) {
      bw.write(html);
    }
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(new FileInputStream(tempFile), StandardCharsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
      }
    }

    tempFile.delete();
  }

}
