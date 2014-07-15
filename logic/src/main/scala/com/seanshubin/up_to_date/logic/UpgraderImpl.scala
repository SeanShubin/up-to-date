package com.seanshubin.up_to_date.logic

class UpgraderImpl extends Upgrader {
  override def performAutomaticUpgrades(recommendations: Recommendations): AutomaticUpgradesPerformed = {
    println("upgrader - Automatic upgrades not supported yet, skipping.")
    null
  }
}
