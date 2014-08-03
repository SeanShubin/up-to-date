package com.seanshubin.up_to_date.logic

trait DependencyUpgradeAnalyzer {
  def recommendUpgrades(poms: Seq[Pom], libraries: Seq[Library]): Seq[Upgrade]

  def findInconsistencies(poms: Seq[Pom]): Map[GroupAndArtifact, Seq[Dependency]]
}
