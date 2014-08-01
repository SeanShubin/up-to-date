package com.seanshubin.up_to_date.logic

trait PomFileUpgrader {
  def performAutomaticUpgradesIfApplicable(upgradesByPom: Map[String, Map[GroupAndArtifact, String]])
}
