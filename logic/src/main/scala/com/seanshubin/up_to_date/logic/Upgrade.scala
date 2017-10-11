package com.seanshubin.up_to_date.logic

case class Upgrade(location: String, group: String, artifact: String, fromVersion: String, toVersion: String) {
  def groupAndArtifact = GroupAndArtifact(group, artifact)

  def groupArtifactVersionFrom = GroupArtifactVersion(group, artifact, fromVersion)

  def groupArtifactVersionTo = GroupArtifactVersion(group, artifact, toVersion)

  def toGroupArtifactSeq = Seq(group, artifact)
}

object Upgrade {
  def groupByLocation(upgrades: Seq[Upgrade]): Map[String, Seq[Upgrade]] = {
    upgrades.groupBy(upgrade => upgrade.location)
  }

  def groupByGroupArtifactVersionFrom(upgrades: Seq[Upgrade]): Map[GroupArtifactVersion, Upgrade] = {
    val entries = for {
      upgrade <- upgrades
      groupArtifactVersion = upgrade.groupArtifactVersionFrom
    } yield {
      (groupArtifactVersion, upgrade)
    }
    val map = entries.toMap
    map
  }
}
