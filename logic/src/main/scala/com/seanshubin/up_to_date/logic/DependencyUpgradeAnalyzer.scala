package com.seanshubin.up_to_date.logic

trait DependencyUpgradeAnalyzer {
  def recommendUpgrades(poms: Seq[Pom], libraries: Seq[Library]): Seq[Upgrade]

  def splitIntoApplyAndIgnore(upgrades: Seq[Upgrade],
                              doNotUpgradeFrom: Set[GroupAndArtifact],
                              doNotUpgradeTo: Set[GroupArtifactVersion]): (Seq[Upgrade], Seq[Upgrade])

  def findInconsistencies(poms: Seq[Pom]): Map[GroupAndArtifact, Seq[Dependency]]
}
