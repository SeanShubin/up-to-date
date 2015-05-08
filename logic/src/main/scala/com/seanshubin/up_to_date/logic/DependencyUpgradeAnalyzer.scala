package com.seanshubin.up_to_date.logic

trait DependencyUpgradeAnalyzer {
  def summary(poms: Seq[Pom],
              upgrades: Map[GroupAndArtifact, List[Upgrade]],
              notFound: Seq[GroupAndArtifact],
              apply: Seq[Upgrade],
              ignore: Seq[Upgrade],
              alreadyUpToDate: Seq[Dependency]): SummaryReport

  def byDependency(upgrades: Seq[Upgrade]): Map[GroupAndArtifact, List[Upgrade]]

  def recommendUpgrades(poms: Seq[Pom], libraries: Seq[Library]): Seq[Upgrade]

  def alreadyUpToDate(poms: Seq[Pom], libraries: Seq[Library]): Seq[Dependency]

  def splitIntoApplyAndIgnore(upgrades: Seq[Upgrade],
                              doNotUpgradeFrom: Set[GroupAndArtifact],
                              doNotUpgradeTo: Set[GroupArtifactVersion]): (Seq[Upgrade], Seq[Upgrade])

  def findInconsistencies(poms: Seq[Pom]): Map[GroupAndArtifact, Seq[Dependency]]
}
