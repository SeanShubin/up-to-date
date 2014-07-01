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

  test("the clock ticks in seconds") {
    val systemClock: SystemClock = new SystemClockImpl
    val startTimeMillis = systemClock.currentTimeMillis
    val currentTimeSeconds = systemClock.currentTimeSeconds
    val endTimeMillis = systemClock.currentTimeMillis
    currentTimeSeconds should be >= (startTimeMillis / 1000)
    currentTimeSeconds should be <= (endTimeMillis / 1000)
  }
}
