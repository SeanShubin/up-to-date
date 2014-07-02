package com.seanshubin.up_to_date.integration

import com.seanshubin.up_to_date.logic.SystemClock
import org.scalatest.{FunSuite, Matchers}

class SystemClockTest extends FunSuite with Matchers {
  test("the clock ticks") {
    val systemClock: SystemClock = new SystemClockImpl
    val startTime: Long = systemClock.currentTimeMillis
    Thread.sleep(1)
    val endTime: Long = systemClock.currentTimeMillis
    endTime should be > startTime
  }
}
