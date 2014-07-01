package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportAutomaticUpgradesPerformed(automaticUpgradesPerformed: AutomaticUpgradesPerformed)

  def reportOutOfDate(outOfDate: OutOfDate)
}
