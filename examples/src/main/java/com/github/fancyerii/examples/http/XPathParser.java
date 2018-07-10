package com.github.fancyerii.examples.http;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.antbrains.nekohtmlparser.NekoHtmlParser;

public class XPathParser {

  public static void main(String[] args) throws Exception {
    String html = ParseByStringFunction.getHtml("禅林");
    NekoHtmlParser parser = new NekoHtmlParser();
    parser.load(html, "UTF8");
    NodeList divs = parser.selectNodes("//DIV[@id='content_left']/DIV[@id]");
    List<BaiduItem> items = new ArrayList<>(divs.getLength());
    for (int i = 0; i < divs.getLength(); i++) {
      Node div = divs.item(i);
      Node a = parser.selectSingleNode("./H3/A", div);
      String title = a.getTextContent().trim();
      String href = a.getAttributes().getNamedItem("href").getTextContent();

      BaiduItem item = new BaiduItem();
      item.title = title;
      item.url = href;
      // TODO extract 摘要
      items.add(item);
    }
  }

}
