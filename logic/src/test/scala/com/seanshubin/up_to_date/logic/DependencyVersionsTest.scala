package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class DependencyVersionsTest extends FunSuite {
  test("add versions") {
    val groupAndArtifact = GroupAndArtifact("group", "artifact")
    val versions = Set("version1", "version2")
    val expected = DependencyVersions(
      map = Map(groupAndArtifact -> versions),
      notFound = Set()
    )
    val actual = DependencyVersions.Empty.addVersions(groupAndArtifact, versions)
    assert(actual === expected)
  }
  test("not found") {
    val groupAndArtifact = GroupAndArtifact("group", "artifact")
    val expected = DependencyVersions(
      map = Map(),
      notFound = Set(groupAndArtifact)
    )
    val actual = DependencyVersions.Empty.addNotFound(groupAndArtifact)
    assert(actual === expected)
  }
}
