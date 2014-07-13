package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class DependencyUpgradeAnalyzerTest extends FunSuite {
  test("recommend upgrades") {
    val existingDependencies = ExistingDependencies(Map(
      "pom.xml" -> Seq(
        PomDependency("com.fasterxml.jackson.module", "jackson-module-scala_2.11", "1.2.3"),
        PomDependency("org.scala-lang", "scala-library", "2.11.1"),
        PomDependency("joda-time", "joda-time", "2.3")),
      "logic/pom.xml" -> Seq(
        PomDependency("com.fasterxml.jackson.module", "jackson-module-scala_2.11", "1.3-rc1"),
        PomDependency("org.scala-lang", "scala-library", "2.10"),
        PomDependency("joda-time", "joda-time", "2.3")),
      "integration/pom.xml" -> Seq(
        PomDependency("com.fasterxml.jackson.module", "jackson-module-scala_2.11", "1.4-rc1"),
        PomDependency("org.scala-lang", "scala-library", "2.11.0"),
        PomDependency("joda-time", "joda-time", "2.3"))
    ))

    val latestDependencies = DependencyVersions(Map(
      SampleData.jacksonId ->
        LocationAndVersions("http://repo1/jackson", Set("1.2.3", "1.3.0", "1.3-rc1", "1.4-rc1")),
      SampleData.scalaId ->
        LocationAndVersions("http://repo1/scala", Set("2.11.1", "2.10", "2.11.0")),
      SampleData.jodaId ->
        LocationAndVersions("http://repo2/joda", Set("2.0", "2.1", "2.2", "2.3", "2.4-rc1"))))

    val dependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
    val actual = dependencyUpgradeAnalyzer.recommend(existingDependencies, latestDependencies)
    //    assert(actual.totalDependencies === expected.totalDependencies)
    //    assert(actual.dependenciesToUpgrade === expected.dependenciesToUpgrade)
    //    assert(actual.versionEntriesToUpgrade === expected.versionEntriesToUpgrade)
    val actualJackson: RecommendationBySource = actual.byGroupAndArtifact(SampleData.jacksonId)
    assert(actualJackson.bestAvailable === "1.3.0")
    assert(actualJackson.repositoryLocation === "http://repo1/jackson")
    assert(actualJackson.byPomLocation.size === 3)
    assert(actualJackson.byPomLocation("pom.xml") === RecommendedVersionBump("1.2.3", Some("1.3.0")))
    assert(actualJackson.byPomLocation("logic/pom.xml") === RecommendedVersionBump("1.3-rc1", Some("1.3.0")))
    assert(actualJackson.byPomLocation("integration/pom.xml") === RecommendedVersionBump("1.4-rc1", None))
    val actualScala: RecommendationBySource = actual.byGroupAndArtifact(SampleData.scalaId)
    assert(actualScala.bestAvailable === "2.11.1")
    assert(actualScala.repositoryLocation === "http://repo1/scala")
    assert(actualScala.byPomLocation.size === 3)
    assert(actualScala.byPomLocation("pom.xml") === RecommendedVersionBump("2.11.1", None))
    assert(actualScala.byPomLocation("logic/pom.xml") === RecommendedVersionBump("2.10", Some("2.11.1")))
    assert(actualScala.byPomLocation("integration/pom.xml") === RecommendedVersionBump("2.11.0", Some("2.11.1")))
    val actualJoda: RecommendationBySource = actual.byGroupAndArtifact(SampleData.jodaId)
    assert(actualJoda.bestAvailable === "2.3")
    assert(actualJoda.repositoryLocation === "http://repo2/joda")
    assert(actualJoda.byPomLocation.size === 3)
    assert(actualJoda.byPomLocation("pom.xml") === RecommendedVersionBump("2.3", None))
    assert(actualJoda.byPomLocation("logic/pom.xml") === RecommendedVersionBump("2.3", None))
    assert(actualJoda.byPomLocation("integration/pom.xml") === RecommendedVersionBump("2.3", None))
  }
}
