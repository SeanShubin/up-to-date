package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportUpgrades(upgrades: Seq[Upgrade])

  def reportInconsistencies(inconsistencies: Map[GroupAndArtifact, Seq[Dependency]])

  def reportPom(poms: Seq[Pom])

  def reportRepository(libraries: Seq[Library])
}
