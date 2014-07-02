package com.seanshubin.up_to_date.integration

import java.nio.charset.StandardCharsets

import com.seanshubin.up_to_date.logic.{FakeNotifications, Http}
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class HttpTest extends FunSuite with EasyMockSugar {
  test("get") {
    val charset = StandardCharsets.UTF_8
    val testWebServer = new TestWebServer
    val notifications = new FakeNotifications()
    testWebServer.fakeStatus = 200
    testWebServer.fakeContent = """{ "result": "ok" }"""
    testWebServer.fakeCharset = charset
    val http: Http = new HttpImpl(charset, notifications)
    val port = testWebServer.start()
    val uri: String = s"http://localhost:$port/foo"
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
    assert(notifications.getCalls === Seq(s"http://localhost:$port/foo"))
  }
}
