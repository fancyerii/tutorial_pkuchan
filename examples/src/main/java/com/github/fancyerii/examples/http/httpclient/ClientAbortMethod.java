/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.github.fancyerii.examples.http.httpclient;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This example demonstrates how to abort an HTTP method before its normal
 * completion.
 */
public class ClientAbortMethod {

  public final static void main(String[] args) throws Exception {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
      HttpGet httpget = new HttpGet(
          "http://apache.communilink.net/httpcomponents/httpclient/binary/httpcomponents-client-4.5.6-bin.tar.gz");

      System.out.println("Executing request " + httpget.getURI());
      CloseableHttpResponse response = httpclient.execute(httpget);
      try {
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        // Do not feel like reading the response body
        // Call abort on the request object
        long length = response.getEntity().getContentLength();
        System.out.println("len: " + length);
        final InputStream is = response.getEntity().getContent();
        new Thread(new Runnable() {

          @Override
          public void run() {
            byte[] buffer = new byte[1024];

            try {
              while (true) {
                int readCount = is.read(buffer);
                System.out.println("read: " + readCount);
                Thread.sleep(1000);
              }

            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              try {
                is.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
          }

        }).start();
        Thread.sleep(10000);
        httpget.abort();
      } finally {
        response.close();
      }
    } finally {
      httpclient.close();
    }
  }

}
