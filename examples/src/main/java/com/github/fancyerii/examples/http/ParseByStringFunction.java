package com.github.fancyerii.examples.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ParseByStringFunction {

  public static void main(String[] args) throws Exception {
    String html = getHtml("禅林");
    parse(html);
  }

  public static List<BaiduItem> parse1(String html) {
    List<BaiduItem> items = new ArrayList<>();
    int idx = 0;
    while (true) {
      idx = html.indexOf("<h3 class=\"t\">", idx);
      if (idx == -1)
        break;
      int endIdx = html.indexOf("</h3>", idx);
      if (endIdx == -1) {
        System.err.println("no end h3 from: " + idx);
        break;
      }
      String s = html.substring(idx + "<h3 class=\"t\">".length(), endIdx);
      System.out.println(s);
      idx = endIdx;
    }
    return items;
  }

  public static List<BaiduItem> parse(String html) {
    System.out.println(html);
    List<BaiduItem> items = new ArrayList<>();
    int idx = 0;
    while (true) {
      int[] idxes = extract(html, idx, "<h3 class=\"t\">", "</h3>");
      if (idxes[0] == -1)
        break;
      String s = html.substring(idxes[0], idxes[1]);
      if (s.contains("百度地图")) {
        System.out.println("here");
      }
      int[] idxes2 = extract(s, 0, ">", "</a>");
      if (idxes2[0] != -1) {
        String title = s.substring(idxes2[0], idxes2[1]);
        System.out.println(title);
      }
      idx = idxes[1];
    }
    return items;
  }

  private static int[] extract(String s, int startIdx, String start, String end) {
    int[] idxes = new int[2];
    int idx = s.indexOf(start, startIdx);
    if (idx == -1) {
      idxes[0] = -1;
      return idxes;
    }
    int endIdx = s.indexOf(end, idx);
    if (endIdx == -1) {
      idxes[0] = -1;
      return idxes;
    }
    idxes[0] = idx + start.length();
    idxes[1] = endIdx;
    return idxes;
  }

  public static String getHtml(String kw) throws Exception {
    String url = "http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&ch=&tn=baiduerr&bar=&wd=";
    url = url + UrlEncodingExample.urlEncoding(kw, "UTF8");
    HttpClient client = HttpClientBuilder.create().build();
    HttpResponse response = client.execute(new HttpGet(url));
    String bodyAsString = EntityUtils.toString(response.getEntity(), "UTF8");
    return bodyAsString;
  }
}
