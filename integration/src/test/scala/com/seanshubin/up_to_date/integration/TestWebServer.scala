package com.seanshubin.up_to_date.integration

import java.net.ServerSocket
import java.nio.charset.Charset
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.server.{Request, Server}

class TestWebServer {
  private var server: Server = _

  var fakeStatus: Int = _
  var fakeContent: String = _
  var fakeCharset: Charset = _
  var actualMethod: String = _
  var actualPath: String = _

  class TestHandler extends AbstractHandler {
    override def handle(target: String,
                        baseRequest: Request,
                        httpServletRequest: HttpServletRequest,
                        httpServletResponse: HttpServletResponse): Unit = {
      actualMethod = httpServletRequest.getMethod
      actualPath = httpServletRequest.getRequestURI
      baseRequest.setHandled(true)
      httpServletResponse.setStatus(fakeStatus)
      httpServletResponse.setCharacterEncoding(fakeCharset.name())
      IoUtil.stringToWriter(fakeContent, httpServletResponse.getWriter)
    }
  }

  def start(): Int = {
    val testHandler = new TestHandler()
    val serverSocket: ServerSocket = new ServerSocket(0)
    val port = serverSocket.getLocalPort
    serverSocket.close()
    server = new Server(port)
    server.setHandler(testHandler)
    server.start()
    port
  }

  def stop() {
    server.stop()
  }
}
