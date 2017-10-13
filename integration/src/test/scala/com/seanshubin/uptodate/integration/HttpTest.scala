package com.seanshubin.uptodate.integration

import java.net.URI
import java.nio.charset.StandardCharsets

import com.seanshubin.uptodate.logic.Http
import org.scalatest.FunSuite

class HttpTest extends FunSuite {
  test("get") {
    val charset = StandardCharsets.UTF_8
    val testWebServer = new TestWebServer
    val notifications = new StubNotifications()
    testWebServer.status = 200
    testWebServer.content = """{ "result": "ok" }"""
    testWebServer.charset = charset
    val http: Http = new HttpImpl(charset, notifications)
    val port = testWebServer.start()
    val uriString: String = s"http://localhost:$port/foo"
    val uri = new URI(uriString)
    val (actualStatusCode, actualContent) = try {
      http.get(uri)
    } finally {
      testWebServer.stop()
    }
    assert(testWebServer.actualMethod === "GET")
    assert(testWebServer.actualPath === "/foo")
    assert(actualStatusCode === 200)
    assert(actualContent === """{ "result": "ok" }""")
    assert(notifications.timeTakenCalls === Seq("GET TIME"))
    assert(notifications.getCalls === Seq(uri))
  }
}
