package com.github.fancyerii.examples.http;

public class BaiduItem {
  public String title;
  public String url;
  public String desc;

  @Override
  public String toString() {
    return "title: " + title + "\t" + "url: " + url + "\tdesc: " + desc;
  }
}
