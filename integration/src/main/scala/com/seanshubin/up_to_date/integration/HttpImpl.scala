package com.seanshubin.up_to_date.integration

import java.net.URI
import java.nio.charset.Charset

import com.seanshubin.up_to_date.logic.{Http, Notifications}
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
      val content = IoUtil.inputStreamToString(inputStream, charset)
      (statusCode, content)
    }
  }
}
