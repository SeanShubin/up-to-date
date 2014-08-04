package com.seanshubin.up_to_date.logic

trait Reporter {
  def reportUpgradesToApply(upgrades: Seq[Upgrade])

  def reportUpgradesToIgnore(upgrades: Seq[Upgrade])

  def reportInconsistencies(inconsistencies: Map[GroupAndArtifact, Seq[Dependency]])

  def reportPom(poms: Seq[Pom])

  def reportRepository(libraries: Seq[Library])

  def reportStatusQuo(upgrades: Seq[Upgrade])

  def reportNotFound(notFound: Seq[GroupAndArtifact])
}
