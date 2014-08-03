package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class DependencyUpgradeAnalyzerTest extends FunSuite {
  test("recommend upgrades") {
    val poms = Seq(Pom("pom.xml", Seq(
      Dependency("pom.xml", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", "1.2.3"),
      Dependency("pom.xml", "org.scala-lang", "scala-library", "2.11.1"),
      Dependency("pom.xml", "joda-time", "joda-time", "2.3"))),
      Pom("logic/pom.xml", Seq(
        Dependency("logic/pom.xml", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", "1.3-rc1"),
        Dependency("logic/pom.xml", "org.scala-lang", "scala-library", "2.10"),
        Dependency("logic/pom.xml", "joda-time", "joda-time", "2.3"))),
      Pom("integration/pom.xml", Seq(
        Dependency("integration/pom.xml", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", "1.4-rc1"),
        Dependency("integration/pom.xml", "org.scala-lang", "scala-library", "2.11.0"),
        Dependency("integration/pom.xml", "joda-time", "joda-time", "2.3")))
    )

    val libraries = Seq(
      Library("http://repo1/jackson", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", Seq("1.2.3", "1.3.0", "1.3-rc1", "1.4-rc1")),
      Library("http://repo1/scala", "org.scala-lang", "scala-library", Seq("2.11.1", "2.10", "2.11.0")),
      Library("http://repo1/joda", "joda-time", "joda-time", Seq("2.0", "2.1", "2.2", "2.3", "2.4-rc1")))

    val dependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
    val actual = dependencyUpgradeAnalyzer.recommendUpgrades(poms, libraries)
    assert(actual.size === 4)
    assert(actual(0).location === "pom.xml")
    assert(actual(0).group === "com.fasterxml.jackson.module")
    assert(actual(0).artifact === "jackson-module-scala_2.11")
    assert(actual(0).fromVersion === "1.2.3")
    assert(actual(0).toVersion === "1.3.0")
    assert(actual(1).location === "logic/pom.xml")
    assert(actual(1).group === "com.fasterxml.jackson.module")
    assert(actual(1).artifact === "jackson-module-scala_2.11")
    assert(actual(1).fromVersion === "1.3-rc1")
    assert(actual(1).toVersion === "1.3.0")
    assert(actual(2).location === "logic/pom.xml")
    assert(actual(2).group === "org.scala-lang")
    assert(actual(2).artifact === "scala-library")
    assert(actual(2).fromVersion === "2.10")
    assert(actual(2).toVersion === "2.11.1")
    assert(actual(3).location === "integration/pom.xml")
    assert(actual(3).group === "org.scala-lang")
    assert(actual(3).artifact === "scala-library")
    assert(actual(3).fromVersion === "2.11.0")
    assert(actual(3).toVersion === "2.11.1")
  }

  test("filter out do not upgrade from") {
    val upgrades = Seq(Upgrade("pom", "group-1", "artifact-1", "version-1", "upgrade-1"))
    val doNotUpgradeFrom = Set(GroupAndArtifact("group-1", "artifact-1"))
    val doNotUpgradeTo = Set[GroupArtifactVersion]()
    val dependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
    val (apply, ignore) = dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(upgrades, doNotUpgradeFrom, doNotUpgradeTo)
    assert(apply.size === 0)
    assert(ignore.size === 1)
  }

  test("filter out do not upgrade to") {
    val upgrades = Seq(Upgrade("pom", "group-1", "artifact-1", "version-1", "upgrade-1"))
    val doNotUpgradeFrom = Set[GroupAndArtifact]()
    val doNotUpgradeTo = Set(GroupArtifactVersion("group-1", "artifact-1", "upgrade-1"))
    val dependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
    val (apply, ignore) = dependencyUpgradeAnalyzer.splitIntoApplyAndIgnore(upgrades, doNotUpgradeFrom, doNotUpgradeTo)
    assert(apply.size === 0)
    assert(ignore.size === 1)
  }
}
