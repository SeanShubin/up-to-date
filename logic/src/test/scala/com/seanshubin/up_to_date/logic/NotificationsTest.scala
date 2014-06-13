package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class NotificationsTest extends FunSuite with EasyMockSugar {
  test("configuration error") {
    //given
    val lines = new ArrayBuffer[String]()
    def emitLine(line: String) = lines.append(line)
    val notifications = new LineEmittingNotifications(emitLine)

    //when
    notifications.errorWithConfiguration(Seq("line 1", "line 2"))

    //then
    assert(lines.size === 3)
    assert(lines(0) === "Unable to launch application due to configuration validation errors")
    assert(lines(1) === "line 1")
    assert(lines(2) === "line 2")
  }
}
