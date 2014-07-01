package com.seanshubin.up_to_date.integration

import com.seanshubin.up_to_date.logic.SystemClock

class SystemClockImpl extends SystemClock {
  override def currentTimeMillis: Long = System.currentTimeMillis()

  override def currentTimeSeconds: Long = System.currentTimeMillis() / 1000
}
