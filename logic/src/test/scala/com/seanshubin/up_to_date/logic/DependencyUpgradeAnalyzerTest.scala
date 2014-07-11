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
        PomDependency("org.scala-lang", "scala-library", "2.11.1"),
        PomDependency("joda-time", "joda-time", "2.3"))
    ))

    val latestDependencies = DependencyVersions(Map(
      GroupAndArtifact("com.fasterxml.jackson.module", "jackson-module-scala_2.11") ->
        LocationAndVersions("http://repo1/jackson", Set("1.2.3", "1.3.0", "1.3-rc1", "1.4-rc1")),
      GroupAndArtifact("org.scala-lang", "scala-library") ->
        LocationAndVersions("http://repo1/scala", Set("2.11.1", "2.10", "2.11.0")),
      GroupAndArtifact("joda-time", "joda-time") ->
        LocationAndVersions("http://repo2/joda", Set("2.0", "2.1", "2.2", "2.3", "2.4-rc1"))))

    val expected = Recommendations(
      totalDependencies = 3,
      dependenciesToUpgrade = 2,
      versionEntriesToUpgrade = 4,
      byGroupAndArtifact = Map(
        GroupAndArtifact("com.fasterxml.jackson.module", "jackson-module-scala_2.11") ->
          RecommendationBySource(latestRelease = "1.3.0", location = "http://repo1/jackson", Map(
            "pom.xml" -> RecommendedVersionBump("1.2.3", Some("1.3.0")),
            "logic/pom.xml" -> RecommendedVersionBump("1.3-rc1", Some("1.3.0")),
            "integration/pom.xml" -> RecommendedVersionBump("1.4-rc1", None))),
        GroupAndArtifact("org.scala-lang", "scala-library") ->
          RecommendationBySource(latestRelease = "2.11.1", location = "http://repo1/scala", Map(
            "pom.xml" -> RecommendedVersionBump("2.11.1", None),
            "logic/pom.xml" -> RecommendedVersionBump("2.10", Some("2.11.1")),
            "integration/pom.xml" -> RecommendedVersionBump("2.11.0", Some("2.11.1")))),
        GroupAndArtifact("joda-time", "joda-time") ->
          RecommendationBySource(latestRelease = "2.3", location = "http://repo2/joda", Map(
            "pom.xml" -> RecommendedVersionBump("2.3", None),
            "logic/pom.xml" -> RecommendedVersionBump("2.3", None),
            "integration/pom.xml" -> RecommendedVersionBump("2.3", None)))))

    val dependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
    val actual = dependencyUpgradeAnalyzer.outOfDate(existingDependencies, latestDependencies)
    assert(actual === expected)
  }
}
