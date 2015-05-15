package com.seanshubin.up_to_date.logic

import java.net.URI

import com.seanshubin.devon.core.devon.DevonMarshaller
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class NotificationsTest extends FunSuite with EasyMockSugar {
  private val dummySystemClock: SystemClock = null
  private val dummyDevonMarshaller: DevonMarshaller = null
  test("configuration error") {
    //given
    val lines = new ArrayBuffer[String]()
    def emitLine(line: String) = lines.append(line)
    val notifications = new LineEmittingNotifications(dummySystemClock, dummyDevonMarshaller, emitLine)

    //when
    notifications.errorWithConfiguration(Seq("config.json"), Seq("line 1", "line 2"))

    //then
    assert(lines.size === 4)
    assert(lines(0) === "config.json")
    assert(lines(1) === "Unable to launch application due to configuration validation errors")
    assert(lines(2) === "line 1")
    assert(lines(3) === "line 2")
  }

  test("time taken") {
    //given
    val lines = new ArrayBuffer[String]()
    val systemClock = mock[SystemClock]
    def emitLine(line: String) = lines.append(line)
    val notifications = new LineEmittingNotifications(systemClock, dummyDevonMarshaller, emitLine)

    expecting {
      systemClock.currentTimeMillis.andReturn(4000)
      systemClock.currentTimeMillis.andReturn(7000)
    }

    //when
    whenExecuting(systemClock) {
      notifications.timeTaken("Total time taken") {}
    }

    //then
    assert(lines.size === 1)
    assert(lines(0) === "Total time taken: 3 seconds")
  }

  test("http get") {
    //given
    val lines = new ArrayBuffer[String]()
    def emitLine(line: String) = lines.append(line)
    val notifications = new LineEmittingNotifications(dummySystemClock, dummyDevonMarshaller, emitLine)

    //when
    notifications.httpGet(new URI("http://localhost"))

    //then
    assert(lines.size === 1)
    assert(lines(0) === "GET: http://localhost")
  }
}
