package com.github.fancyerii.examples.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class UrlEncodingExample {

  public static void main(String[] args) throws Exception {
    String kw = "天气";
    String url = "http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&ch=&tn=baiduerr&bar=&wd=";
    url = url + urlEncoding(kw, "UTF8");
    System.out.println(url);
    HttpClient client = HttpClientBuilder.create().build();
    HttpResponse response = client.execute(new HttpGet(url));
    String bodyAsString = EntityUtils.toString(response.getEntity(), "UTF8");
    System.out.println(bodyAsString);
  }

  public static String urlEncoding(String s, String encoding) {
    try {
      return java.net.URLEncoder.encode(s, encoding);
    } catch (UnsupportedEncodingException e) {
      return s;
    }
  }
}
