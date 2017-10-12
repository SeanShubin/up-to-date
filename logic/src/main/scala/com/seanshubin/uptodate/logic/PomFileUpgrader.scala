package com.seanshubin.uptodate.logic

trait PomFileUpgrader {
  def performAutomaticUpgradesIfApplicable(upgrades: Seq[Upgrade])
}
