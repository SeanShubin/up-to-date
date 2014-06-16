package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed, endTime: Long)

  def reportOutOfDate(outOfDate: OutOfDate)
}
