package com.seanshubin.uptodate.integration

import com.seanshubin.uptodate.logic.SystemClock

class SystemClockImpl extends SystemClock {
  override def currentTimeMillis: Long = System.currentTimeMillis()
}
