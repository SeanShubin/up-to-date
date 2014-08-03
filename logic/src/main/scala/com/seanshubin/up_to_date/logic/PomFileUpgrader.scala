package com.seanshubin.up_to_date.logic

trait PomFileUpgrader {
  def performAutomaticUpgradesIfApplicable(upgrades: Seq[Upgrade])
}
