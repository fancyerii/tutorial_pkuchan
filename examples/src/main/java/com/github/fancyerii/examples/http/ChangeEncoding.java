package com.github.fancyerii.examples.http;

import java.io.IOException;

import com.antbrains.httpclientfetcher.FileTools;

public class ChangeEncoding {

  public static void main(String[] args) throws IOException {
    String s = FileTools.readFile("test.txt", "GBK");
    System.out.println(s.substring(0, 100));
    FileTools.writeFile("test.txt.utf8", s, "UTF8");
  }

}
