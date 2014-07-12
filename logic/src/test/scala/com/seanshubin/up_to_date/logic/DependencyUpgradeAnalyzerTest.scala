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

    val expected = SampleData.recommendations
    val dependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
    val actual = dependencyUpgradeAnalyzer.recommend(existingDependencies, latestDependencies)
    assert(actual === expected)
  }
}
