package com.seanshubin.up_to_date.integration

import org.scalatest.FunSuite
import java.nio.charset.StandardCharsets
import com.seanshubin.up_to_date.logic.Http

class HttpTest extends FunSuite {
  test("get") {
    val charset = StandardCharsets.UTF_8
    val testWebServer = new TestWebServer

    testWebServer.fakeStatus = 200
    testWebServer.fakeContent = """{ "result": "ok" }"""
    testWebServer.fakeCharset = charset
    testWebServer.start()

    val http: Http = new HttpImpl(charset)
    val (actualStatusCode, actualContent) = http.get(s"http://localhost:${testWebServer.port}/foo")

    assert(testWebServer.actualMethod === "GET")
    assert(testWebServer.actualPath === "/foo")

    assert(actualStatusCode === 200)
    assert(actualContent === """{ "result": "ok" }""")
    testWebServer.stop()
  }
}
