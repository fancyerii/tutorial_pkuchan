package com.github.fancyerii.examples.http;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.http.HttpResponse;

import com.antbrains.httpclientfetcher.HttpClientFetcher;

public class HttpClientFetcherExample {

  public static void main(String[] args) throws Exception {
    HttpClientFetcher fetcher = new HttpClientFetcher("");
    fetcher.init();
    Object[] arr = fetcher.httpGetStream(
        "http://www.fjzjg.com/modules/article/packdown.php?id=768&type=txt&fname=%BA%A9%C9%BD%C0%CF%C8%CB%C3%CE%D3%CE%BC%AF");

    HttpResponse response = (HttpResponse) arr[0];
    long length=response.getEntity().getContentLength();
    String contentType=response.getEntity().getContentType().toString();
    System.out.println("len: "+length);
    System.out.println("contentType: "+contentType);
    InputStream is = response.getEntity().getContent();
    Files.copy(is, Paths.get("./test.txt"), StandardCopyOption.REPLACE_EXISTING);
    long realLength=new File("./test.txt").length();
    System.out.println("realDownload len: "+realLength);
    fetcher.close();
  }

}
