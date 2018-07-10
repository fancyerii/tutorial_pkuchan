package com.github.fancyerii.examples.http.fjzjg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files; 
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.antbrains.httpclientfetcher.FileTools;
import com.antbrains.httpclientfetcher.HttpClientFetcher;
import com.antbrains.nekohtmlparser.NekoHtmlParser;
import com.google.gson.Gson;

public class BookCrawler {
  protected static Logger logger=Logger.getLogger(BookCrawler.class);
  
  private Gson gson=new Gson();
  private HttpClientFetcher fetcher;
  private File dataDir;
  
  public BookCrawler(){
    fetcher=new HttpClientFetcher(BookCrawler.class.getName());
    fetcher.setMaxTotalConnection(10);
    fetcher.setMaxConnectionPerRoute(10);
    fetcher.init();
    dataDir=new File("./books");
    if(dataDir.exists() && !dataDir.isDirectory()){
      throw new RuntimeException("dataDir is not a dir: "+dataDir.getAbsolutePath());
    }
    
    dataDir.mkdirs();
  }
  
  public void crawl(){
    int totalPage=10;
    int curPage=1;
    while(curPage<=totalPage){      
      String url="http://www.fjzjg.com/modules/article/articlelist.php?class=1&charset=gbk&page="+curPage;
      logger.info("crawl: "+url);
      String html=null;
      try {
        html=fetcher.httpGet(url, 3);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
      if(html==null){
        logger.warn("can't get: "+url);
        continue;
      }
      NekoHtmlParser parser=new NekoHtmlParser();
      try {
        parser.load(html, "UTF8");
      } catch (Exception e) {
        logger.error(e.getMessage(),e);
        continue;
      }
      if(curPage==1){
        int total=this.parseTotalPage(parser);
        if(total!=-1){
          totalPage=total;
        }
      }
      List<String> urls=this.parseDetailPages(url, parser);
      for(String u:urls){
        this.processDetailPage(u);
      }
    }
  }
  
  private void processDetailPage(String url){
    logger.info("crawl detail: "+url);
    String html=null;
    try {
      html=fetcher.httpGet(url, 3);
    } catch (Exception e) {
      logger.error(e.getMessage(), e); 
    }
    if(html==null){
      logger.warn("can't get detail: "+url);
      return;
    }
    

    BookData book = this.parseBook(url, html);
    if(book==null){
      logger.warn("can't parse: "+url);
      return;
    }
    //Download 
    File bookFile=new File(this.dataDir, book.id+".txt");
    try {
      Object[] arr = fetcher.httpGetStream(book.dlUrl);
      HttpResponse response = (HttpResponse) arr[0]; 
      long length=response.getEntity().getContentLength();
      InputStream is = response.getEntity().getContent();
      byte[] bytes=EntityUtils.toByteArray(response.getEntity());
      if(bytes.length!=length){
        logger.warn("length mismatch: "+book.dlUrl);
        return;
      }
      String s=new String(bytes, "GBK");
      FileTools.writeFile(bookFile.getAbsolutePath(), s, "UTF8");
      
    } catch (Exception e) { 
      logger.error(e.getLocalizedMessage());
    }
    
    File bookMetaFile=new File(this.dataDir, book.id+".dat");
    String json=gson.toJson(book);
    try {
      FileTools.writeFile(bookMetaFile.getAbsolutePath(), json, "UTF8");
    } catch (IOException e) {
      logger.error(e.getMessage(),e);
    }
    
    
  }
  
  private String parseIdFromUrl(String url){
    try {
      URL u=new URL(url);
      String query=u.getQuery();
      String[] params=query.split("&");
      for(String param:params){
        String[] kv=param.split("=");
        if(kv[0].equals("id")){
          return kv[1];
        }
      }
    } catch (MalformedURLException e) { 
    }
    return null;
  }
  
  private BookData parseBook(String url, String html){
    NekoHtmlParser parser=new NekoHtmlParser();
    try {
      parser.load(html, "UTF8");
    } catch (Exception e) {
      logger.error(e.getMessage(),e );
      return null;
    }
    String id=this.parseIdFromUrl(url);
    if(id==null){
      return null;
    }
    BookData book=new BookData();
    book.id=id;
    String title=parser.getNodeText("//DIV[@id='content']//TD[@align='center']/SPAN");
    book.title=title;
    String href=parser.getNodeText("//A[text()='TXT 全文']/@href");
    String bookUrl=this.getAbsoluteUrl(url, href);
    book.dlUrl=bookUrl;
    
    
    return book;
  }
  
  private int parseTotalPage(NekoHtmlParser parser){
    String text=parser.getNodeText("//A[@class='last']");
    try{
      return Integer.parseInt(text);
    }catch(Exception e){
      return -1;
    }
  }
  
  private String getAbsoluteUrl(String baseUrl, String url) {
    try {
      URL u = new URL(baseUrl);
      URL uu = new URL(u, url);
      return uu.toString();
    } catch (MalformedURLException e) {
      return null;
    }

  }
  
  private List<String> parseDetailPages(String baseUrl, NekoHtmlParser parser){
    List<String> urls=new ArrayList<>();

    
    NodeList list=parser.selectNodes("//DIV[@id='content']//TR/TD[@class='odd']/A");
    for(int i=0;i<list.getLength();i++){
      Node n=list.item(i);
      String href=n.getAttributes().getNamedItem("href").getTextContent();
      String url=this.getAbsoluteUrl(baseUrl, href);
      urls.add(url);
    }
    
    return urls;
  }
  
  public static void main(String[] args) {
    BookCrawler bc=new BookCrawler();
    bc.crawl();
  }

}
