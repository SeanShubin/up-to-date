package com.seanshubin.uptodate.logic

trait Reporter {
  def reportSummary(summary: SummaryReport)

  def reportByDependency(artifactToUpgrade: Map[GroupAndArtifact, List[Upgrade]])

  def reportUpgradesToApply(upgrades: Seq[Upgrade])

  def reportUpgradesToIgnore(upgrades: Seq[Upgrade])

  def reportInconsistencies(inconsistencies: Map[GroupAndArtifact, Seq[Dependency]])

  def reportPom(poms: Seq[Pom])

  def reportRepository(libraries: Seq[Library])

  def reportStatusQuo(upgrades: Seq[Upgrade])

  def reportNotFound(notFound: Seq[GroupAndArtifact])

  def reportUnexpandedPom(unexpandedPoms: Seq[Pom])

  def reportPropertyConflicts(propertyConflicts: Map[String, Set[String]])
}
