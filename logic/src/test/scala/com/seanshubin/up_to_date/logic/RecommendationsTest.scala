package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class RecommendationsTest extends FunSuite {
  val bump1 = RecommendedVersionBump("from-1", Some("to-1"))
  val bump2 = RecommendedVersionBump("from-2", Some("to-2"))
  val bump3 = RecommendedVersionBump("from-3", Some("to-3"))
  val bump4 = RecommendedVersionBump("from-4", Some("to-4"))
  test("collect pom files") {
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
    val actual = recommendations.pomFiles
    val expected = Set("pom-1", "pom-2", "pom-3")
    assert(actual === expected)
  }
}
