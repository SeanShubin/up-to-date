package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class RecommendationsTest extends FunSuite {
  test("recommendations to upgrades by pom") {
    val bump1 = RecommendedVersionBump("from-1", Some("to-1"))
    val bump2 = RecommendedVersionBump("from-2", Some("to-2"))
    val bump3 = RecommendedVersionBump("from-3", Some("to-3"))
    val bump4 = RecommendedVersionBump("from-4", Some("to-4"))
    val byPomLocation1 = Map(
      "pom-1" -> bump1,
      "pom-2" -> bump2
    )
    val byPomLocation2 = Map(
      "pom-2" -> bump3,
      "pom-3" -> bump4
    )
    val groupAndArtifact1 = GroupAndArtifact("group-1", "artifact-1")
    val recommendationBySource1 = RecommendationBySource("best-available-1", "repository-location-1", byPomLocation1)
    val groupAndArtifact2 = GroupAndArtifact("group-2", "artifact-2")
    val recommendationBySource2 = RecommendationBySource("best-available-2", "repository-location-2", byPomLocation2)
    val byGroupAndArtifact = Map(
      groupAndArtifact1 -> recommendationBySource1,
      groupAndArtifact2 -> recommendationBySource2
    )
    val recommendations = Recommendations(byGroupAndArtifact)
    val actual = recommendations.upgradesByPom
    val expected = Map(
      "pom-1" -> Map(groupAndArtifact1 -> "to-1"),
      "pom-2" -> Map(groupAndArtifact1 -> "to-2", groupAndArtifact2 -> "to-3"),
      "pom-3" -> Map(groupAndArtifact2 -> "to-4"))
    assert(actual === expected)
  }
  test("recommendations to upgrades") {
    val bump1 = RecommendedVersionBump("from-1", Some("to-1"))
    val bump2 = RecommendedVersionBump("from-2", Some("to-2"))
    val bump3 = RecommendedVersionBump("from-3", Some("to-3"))
    val bump4 = RecommendedVersionBump("from-4", Some("to-4"))
    val bump5 = RecommendedVersionBump("from-5", Some("to-5"))
    val bump6 = RecommendedVersionBump("from-6", Some("to-6"))
    val bump7 = RecommendedVersionBump("from-7", Some("to-7"))
    val byPomLocation1 = Map(
      "pom-1" -> bump1,
      "pom-2" -> bump2
    )
    val byPomLocation2 = Map(
      "pom-2" -> bump3,
      "pom-3" -> bump4
    )
    val byPomLocation3 = Map(
      "pom-1" -> bump5,
      "pom-2" -> bump6,
      "pom-3" -> bump7
    )
    val groupAndArtifact1 = GroupAndArtifact("group-1", "artifact-1")
    val recommendationBySource1 = RecommendationBySource("best-available-1", "repository-location-1", byPomLocation1)
    val groupAndArtifact2 = GroupAndArtifact("group-2", "artifact-2")
    val recommendationBySource2 = RecommendationBySource("best-available-2", "repository-location-2", byPomLocation2)
    val groupAndArtifact3 = GroupAndArtifact("group-3", "artifact-3")
    val recommendationBySource3 = RecommendationBySource("best-available-3", "repository-location-3", byPomLocation3)
    val byGroupAndArtifact = Map(
      groupAndArtifact1 -> recommendationBySource1,
      groupAndArtifact2 -> recommendationBySource2,
      groupAndArtifact3 -> recommendationBySource3
    )
    val recommendations = Recommendations(byGroupAndArtifact)
    val doNotUpgradeFrom: Set[GroupAndArtifact] = Set()
    val doNotUpgradeTo: Set[GroupArtifactVersion] = Set()
    val ignoreToPreserveStatusQuo: Seq[Seq[String]] = Seq()
    val actual = recommendations.toUpgrades(doNotUpgradeFrom, doNotUpgradeTo)
    val expectedUpgradesByPom = Map(
      "pom-1" -> Map(
        GroupAndArtifact("group-1", "artifact-1") -> "to-1",
        GroupAndArtifact("group-3", "artifact-3") -> "to-5"),
      "pom-2" -> Map(
        GroupAndArtifact("group-1", "artifact-1") -> "to-2",
        GroupAndArtifact("group-2", "artifact-2") -> "to-3",
        GroupAndArtifact("group-3", "artifact-3") -> "to-6"),
      "pom-3" -> Map(
        GroupAndArtifact("group-2", "artifact-2") -> "to-4",
        GroupAndArtifact("group-3", "artifact-3") -> "to-7"))
    val expectedSkippedUpgradesByPom: Map[String, Map[GroupArtifactVersion, String]] = Map()
    val expected = Upgrades(
      expectedUpgradesByPom,
      expectedSkippedUpgradesByPom,
      doNotUpgradeFrom,
      doNotUpgradeTo,
      ignoreToPreserveStatusQuo)
    assert(actual === expected)
  }
}
