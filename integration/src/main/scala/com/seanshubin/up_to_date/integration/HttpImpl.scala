package com.seanshubin.up_to_date.integration

import com.seanshubin.up_to_date.logic.Http
import java.net.URI
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import com.seanshubin.up_to_date.conversion.Conversion
import java.nio.charset.Charset

class HttpImpl(charset: Charset) extends Http {
  override def get(uriString: String): (Int, String) = {
    val uri: URI = new URI(uriString)
    val request = new HttpGet(uri)
    val httpClient = HttpClients.createDefault()
    val httpResponse = httpClient.execute(request)
    val statusCode = httpResponse.getStatusLine.getStatusCode
    val inputStream = httpResponse.getEntity.getContent
    val content = Conversion.inputStreamToString(inputStream, charset)
    (statusCode, content)
  }
}
