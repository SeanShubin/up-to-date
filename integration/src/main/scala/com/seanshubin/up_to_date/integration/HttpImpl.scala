package com.seanshubin.up_to_date.integration

import java.io.InputStreamReader
import java.net.URI
import java.nio.charset.Charset

import com.seanshubin.up_to_date.logic.{Http, Notifications, ReaderIterator}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

class HttpImpl(charset: Charset, notifications: Notifications) extends Http {
  override def get(uriString: String): (Int, String) = {
    notifications.timeTaken("GET TIME") {
      notifications.httpGet(uriString)
      val uri: URI = new URI(uriString)
      val request = new HttpGet(uri)
      val httpClient = HttpClients.createDefault()
      val httpResponse = httpClient.execute(request)
      val statusCode = httpResponse.getStatusLine.getStatusCode
      val inputStream = httpResponse.getEntity.getContent
      val reader = new InputStreamReader(inputStream, charset)
      val content = new ReaderIterator(reader).toSeq.mkString
      (statusCode, content)
    }
  }
}
