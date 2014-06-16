package com.seanshubin.up_to_date.logic

trait Upgrader {
  def performAutomaticUpgrades(outOfDate: OutOfDate): AutomaticUpgradesPerformed
}
