package com.seanshubin.up_to_date.integration

import java.nio.charset.Charset
import java.net.ServerSocket
import org.eclipse.jetty.server.{Request, Server}
import com.seanshubin.up_to_date.conversion.Conversion
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

class TestWebServer {
  private var server: Server = _

  var fakeStatus: Int = _
  var fakeContent: String = _
  var fakeCharset: Charset = _
  var actualMethod: String = _
  var actualPath: String = _
  var port: Int = _

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
      Conversion.stringToWriter(fakeContent, httpServletResponse.getWriter)
    }
  }

  def start() {
    val testHandler = new TestHandler()
    val serverSocket: ServerSocket = new ServerSocket(0)
    port = serverSocket.getLocalPort
    serverSocket.close()
    server = new Server(port)
    server.setHandler(testHandler)
    server.start()
  }

  def stop() {
    server.stop()
  }
}
