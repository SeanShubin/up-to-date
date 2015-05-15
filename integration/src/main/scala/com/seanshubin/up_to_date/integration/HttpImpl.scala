package com.seanshubin.up_to_date.integration

import java.io.InputStreamReader
import java.net.URI
import java.nio.charset.Charset

import com.google.api.client.http._
import com.google.api.client.http.javanet.NetHttpTransport
import com.seanshubin.up_to_date.logic.{Http, Notifications, ReaderIterator}

class HttpImpl(charset: Charset, notifications: Notifications) extends Http {
  val httpTransport: HttpTransport = new NetHttpTransport()
  val factory: HttpRequestFactory = httpTransport.createRequestFactory()

  override def get(uri: URI): (Int, String) = {
    notifications.timeTaken("GET TIME") {
      notifications.httpGet(uri)
      val request = factory.buildGetRequest(new GenericUrl(uri))
      request.setThrowExceptionOnExecuteError(false)
      val httpResponse = request.execute()
      val statusCode = httpResponse.getStatusCode
      val inputStream = httpResponse.getContent
      val reader = new InputStreamReader(inputStream, charset)
      val content = new ReaderIterator(reader).toSeq.mkString
      (statusCode, content)
    }
  }
}
