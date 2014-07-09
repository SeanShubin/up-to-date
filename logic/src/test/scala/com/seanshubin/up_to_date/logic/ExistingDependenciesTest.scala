package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class ExistingDependenciesTest extends FunSuite {
  test("to group and artifact set") {
    val dependency1 = PomDependency("group1", "artifact1", "version1")
    val dependency2 = PomDependency("group2", "artifact2", "version2")
    val existingDependencies = ExistingDependencies(Map(
      "location1" -> Seq(dependency1),
      "location2" -> Seq(dependency2)))
    val expected = Set(GroupAndArtifact("group1", "artifact1"), GroupAndArtifact("group2", "artifact2"))
    val actual = existingDependencies.toGroupAndArtifactSet
    assert(actual === expected)
  }
}
