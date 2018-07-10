package com.github.fancyerii.examples.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class BasicHttpGetExample {

  public static void main(String[] args) throws Exception {
    HttpClient client = HttpClientBuilder.create().build();
    HttpResponse response = client.execute(new HttpGet("http://www.baidu.com/"));
    int statusCode = response.getStatusLine().getStatusCode();
    String reason = response.getStatusLine().getReasonPhrase();
    System.out.println(statusCode + "/" + reason);
    String bodyAsString = EntityUtils.toString(response.getEntity(), "UTF8");
    System.out.println(bodyAsString);
  }

}
