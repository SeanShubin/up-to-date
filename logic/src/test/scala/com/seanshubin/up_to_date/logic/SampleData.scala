package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

object SampleData {
  val configurationJsonComplete = ConfigurationJson(
    pomFileName = Some("pom.xml"),
    directoriesToSearch = Some(Seq("pom directory 1", "pom directory 2")),
    directoryNamesToSkip = Some(Seq("skip this directory")),
    mavenRepositories = Some(Seq("maven repository 1", "maven repository 2")),
    doNotUpgradeFrom = Some(Seq(Seq("group 3", "artifact 3"), Seq("group 4", "artifact 4"))),
    doNotUpgradeTo = Some(Seq(Seq("group 5", "artifact 5", "version 5"), Seq("group 6", "artifact 6", "version 6"))),
    automaticallyUpgrade = Some(true),
    reportDirectory = Some("report directory"),
    cacheDirectory = Some("cache directory"),
    cacheExpire = Some("5 days"))
  val validConfiguration = ValidConfiguration(
    pomFileName = "pom.xml",
    directoriesToSearch = Seq(Paths.get("pom directory 1"), Paths.get("pom directory 2")),
    directoryNamesToSkip = Seq("skip this directory"),
    mavenRepositories = Seq("maven repository 1", "maven repository 2"),
    doNotUpgradeFrom = Set(GroupAndArtifact("group 3", "artifact 3"), GroupAndArtifact("group 4", "artifact 4")),
    doNotUpgradeTo = Set(GroupArtifactVersion("group 5", "artifact 5", "version 5"), GroupArtifactVersion("group 6", "artifact 6", "version 6")),
    automaticallyUpgrade = true,
    reportDirectory = Paths.get("report directory"),
    cacheDirectory = Paths.get("cache directory"),
    cacheExpireMilliseconds = DurationFormat.MillisecondsFormat.parse("5 days")
  )

  val dependency1_1 = Dependency("one/pom.xml", "group1", "artifact1", "version1")
  val dependency1_2 = Dependency("one/pom.xml", "group2", "artifact2", "version2")
  val dependency1_3 = Dependency("one/pom.xml", "group3", "artifact3", "version3")
  val dependency2_1 = Dependency("two/pom.xml", "group2", "artifact2", "version2")
  val dependency2_2 = Dependency("two/pom.xml", "group3", "artifact3", "version3")
  val dependency2_3 = Dependency("two/pom.xml", "group4", "artifact4", "version4")
  val dependency3_1 = Dependency("three/pom.xml", "group3", "artifact3", "version3")
  val dependency3_2 = Dependency("three/pom.xml", "group4", "artifact4", "version4")
  val dependency3_3 = Dependency("three/pom.xml", "group5", "artifact5", "version5")

  val dependencies1 = Seq(dependency1_1, dependency1_2, dependency1_3)
  val dependencies2 = Seq(dependency2_1, dependency2_2, dependency2_3)
  val dependencies3 = Seq(dependency3_1, dependency3_2, dependency3_3)

  val pom1 = Pom("one/pom.xml", dependencies1)
  val pom2 = Pom("two/pom.xml", dependencies2)
  val pom3 = Pom("three/pom.xml", dependencies3)
  val poms: Seq[Pom] = Seq(pom1, pom2, pom3)

  val library1 = Library("repo1", "group1", "artifact1", Seq("version1-1", "version1-2", "version1-3"))
  val library2 = Library("repo1", "group2", "artifact2", Seq("version2-1", "version2-2", "version2-3"))
  val library3 = Library("repo1", "group3", "artifact3", Seq("version3-1", "version3-2", "version3-3"))
  val library4 = Library("repo2", "group4", "artifact4", Seq("version4-1", "version4-2", "version4-3"))
  val library5 = Library("repo2", "group5", "artifact5", Seq("version5-1", "version5-2", "version5-3"))
  val libraries: Seq[Library] = Seq(
    library1,
    library2,
    library3,
    library4,
    library5
  )
  val notFound: Seq[GroupAndArtifact] = Seq(
    dependency3_3.groupAndArtifact
  )

  val upgrades = Seq(
    Upgrade("one/pom.xml", "group1", "artifact1", "version1", "version1-3"),
    Upgrade("one/pom.xml", "group2", "artifact2", "version2", "version2-3"),
    Upgrade("one/pom.xml", "group3", "artifact3", "version3", "version3-3"),
    Upgrade("two/pom.xml", "group2", "artifact2", "version2", "version2-3"),
    Upgrade("two/pom.xml", "group3", "artifact3", "version3", "version3-3"),
    Upgrade("two/pom.xml", "group4", "artifact4", "version4", "version4-3"),
    Upgrade("three/pom.xml", "group3", "artifact3", "version3", "version3-3"),
    Upgrade("three/pom.xml", "group4", "artifact4", "version4", "version4-3"),
    Upgrade("three/pom.xml", "group5", "artifact5", "version5", "version5-3")
  )

  val applyUpgrades = Seq(
    Upgrade("one/pom.xml", "group1", "artifact1", "version1", "version1-3"),
    Upgrade("one/pom.xml", "group2", "artifact2", "version2", "version2-3"),
    Upgrade("one/pom.xml", "group3", "artifact3", "version3", "version3-3"),
    Upgrade("three/pom.xml", "group3", "artifact3", "version3", "version3-3"),
    Upgrade("three/pom.xml", "group4", "artifact4", "version4", "version4-3"),
    Upgrade("three/pom.xml", "group5", "artifact5", "version5", "version5-3")
  )

  val ignoreUpgrades = Seq(
    Upgrade("two/pom.xml", "group2", "artifact2", "version2", "version2-3"),
    Upgrade("two/pom.xml", "group3", "artifact3", "version3", "version3-3"),
    Upgrade("two/pom.xml", "group4", "artifact4", "version4", "version4-3")
  )

  val inconsistencies = Map(
    GroupAndArtifact("group1", "artifact1") -> Seq(
      Dependency("one/pom.xml", "group1", "artifact1", "version1"),
      Dependency("one/pom.xml", "group1", "artifact1", "version2")),
    GroupAndArtifact("group2", "artifact2") -> Seq(
      Dependency("one/pom.xml", "group2", "artifact2", "version1"),
      Dependency("one/pom.xml", "group2", "artifact2", "version2"))
  )

  val dependency1: Dependency = Dependency("pom-1.xml", "org.scala-lang", "scala-library", "2.11.1")
  val dependency2: Dependency = Dependency("pom-1.xml", "joda-time", "joda-time", "2.3")
  val pomReport =
    """{
      |  "one/pom.xml" : {
      |    "location" : "one/pom.xml",
      |    "dependencies" : [ {
      |      "location" : "one/pom.xml",
      |      "group" : "group1",
      |      "artifact" : "artifact1",
      |      "version" : "version1"
      |    }, {
      |      "location" : "one/pom.xml",
      |      "group" : "group2",
      |      "artifact" : "artifact2",
      |      "version" : "version2"
      |    }, {
      |      "location" : "one/pom.xml",
      |      "group" : "group3",
      |      "artifact" : "artifact3",
      |      "version" : "version3"
      |    } ]
      |  },
      |  "two/pom.xml" : {
      |    "location" : "two/pom.xml",
      |    "dependencies" : [ {
      |      "location" : "two/pom.xml",
      |      "group" : "group2",
      |      "artifact" : "artifact2",
      |      "version" : "version2"
      |    }, {
      |      "location" : "two/pom.xml",
      |      "group" : "group3",
      |      "artifact" : "artifact3",
      |      "version" : "version3"
      |    }, {
      |      "location" : "two/pom.xml",
      |      "group" : "group4",
      |      "artifact" : "artifact4",
      |      "version" : "version4"
      |    } ]
      |  },
      |  "three/pom.xml" : {
      |    "location" : "three/pom.xml",
      |    "dependencies" : [ {
      |      "location" : "three/pom.xml",
      |      "group" : "group3",
      |      "artifact" : "artifact3",
      |      "version" : "version3"
      |    }, {
      |      "location" : "three/pom.xml",
      |      "group" : "group4",
      |      "artifact" : "artifact4",
      |      "version" : "version4"
      |    }, {
      |      "location" : "three/pom.xml",
      |      "group" : "group5",
      |      "artifact" : "artifact5",
      |      "version" : "version5"
      |    } ]
      |  }
      |}""".stripMargin
  val repositoryReport =
    """{
      |  "repo2" : [ {
      |    "location" : "repo2",
      |    "group" : "group4",
      |    "artifact" : "artifact4",
      |    "versions" : [ "version4-1", "version4-2", "version4-3" ]
      |  }, {
      |    "location" : "repo2",
      |    "group" : "group5",
      |    "artifact" : "artifact5",
      |    "versions" : [ "version5-1", "version5-2", "version5-3" ]
      |  } ],
      |  "repo1" : [ {
      |    "location" : "repo1",
      |    "group" : "group1",
      |    "artifact" : "artifact1",
      |    "versions" : [ "version1-1", "version1-2", "version1-3" ]
      |  }, {
      |    "location" : "repo1",
      |    "group" : "group2",
      |    "artifact" : "artifact2",
      |    "versions" : [ "version2-1", "version2-2", "version2-3" ]
      |  }, {
      |    "location" : "repo1",
      |    "group" : "group3",
      |    "artifact" : "artifact3",
      |    "versions" : [ "version3-1", "version3-2", "version3-3" ]
      |  } ]
      |}""".stripMargin
  val upgradesReport =
    """{
      |  "two/pom.xml" : [ {
      |    "location" : "two/pom.xml",
      |    "group" : "group2",
      |    "artifact" : "artifact2",
      |    "fromVersion" : "version2",
      |    "toVersion" : "version2-3"
      |  }, {
      |    "location" : "two/pom.xml",
      |    "group" : "group3",
      |    "artifact" : "artifact3",
      |    "fromVersion" : "version3",
      |    "toVersion" : "version3-3"
      |  }, {
      |    "location" : "two/pom.xml",
      |    "group" : "group4",
      |    "artifact" : "artifact4",
      |    "fromVersion" : "version4",
      |    "toVersion" : "version4-3"
      |  } ],
      |  "one/pom.xml" : [ {
      |    "location" : "one/pom.xml",
      |    "group" : "group1",
      |    "artifact" : "artifact1",
      |    "fromVersion" : "version1",
      |    "toVersion" : "version1-3"
      |  }, {
      |    "location" : "one/pom.xml",
      |    "group" : "group2",
      |    "artifact" : "artifact2",
      |    "fromVersion" : "version2",
      |    "toVersion" : "version2-3"
      |  }, {
      |    "location" : "one/pom.xml",
      |    "group" : "group3",
      |    "artifact" : "artifact3",
      |    "fromVersion" : "version3",
      |    "toVersion" : "version3-3"
      |  } ],
      |  "three/pom.xml" : [ {
      |    "location" : "three/pom.xml",
      |    "group" : "group3",
      |    "artifact" : "artifact3",
      |    "fromVersion" : "version3",
      |    "toVersion" : "version3-3"
      |  }, {
      |    "location" : "three/pom.xml",
      |    "group" : "group4",
      |    "artifact" : "artifact4",
      |    "fromVersion" : "version4",
      |    "toVersion" : "version4-3"
      |  }, {
      |    "location" : "three/pom.xml",
      |    "group" : "group5",
      |    "artifact" : "artifact5",
      |    "fromVersion" : "version5",
      |    "toVersion" : "version5-3"
      |  } ]
      |}""".stripMargin
  val inconsistencyReport =
    """{
      |  "GroupAndArtifact(group1,artifact1)" : [ {
      |    "location" : "one/pom.xml",
      |    "group" : "group1",
      |    "artifact" : "artifact1",
      |    "version" : "version1"
      |  }, {
      |    "location" : "one/pom.xml",
      |    "group" : "group1",
      |    "artifact" : "artifact1",
      |    "version" : "version2"
      |  } ],
      |  "GroupAndArtifact(group2,artifact2)" : [ {
      |    "location" : "one/pom.xml",
      |    "group" : "group2",
      |    "artifact" : "artifact2",
      |    "version" : "version1"
      |  }, {
      |    "location" : "one/pom.xml",
      |    "group" : "group2",
      |    "artifact" : "artifact2",
      |    "version" : "version2"
      |  } ]
      |}""".stripMargin

  val notFoundReport =
    """[ {
      |  "group" : "group5",
      |  "artifact" : "artifact5"
      |} ]""".stripMargin

  val sampleMetadataContents =
    """<?xml version="1.0" encoding="UTF-8"?>
      |<metadata modelVersion="1.1.0">
      |  <groupId>commons-io</groupId>
      |  <artifactId>commons-io</artifactId>
      |  <version>1.4</version>
      |  <versioning>
      |    <latest>20030203.000550</latest>
      |    <release>20030203.000550</release>
      |    <versions>
      |      <version>0.1</version>
      |      <version>1.0</version>
      |      <version>1.1</version>
      |      <version>1.2</version>
      |      <version>1.3</version>
      |      <version>1.3.1</version>
      |      <version>1.3.2</version>
      |      <version>1.4</version>
      |      <version>1.4-backport-IO-168</version>
      |      <version>2.0</version>
      |      <version>2.0.1</version>
      |      <version>2.1</version>
      |      <version>2.2</version>
      |      <version>2.3</version>
      |      <version>2.4</version>
      |      <version>2.5-SNAPSHOT</version>
      |      <version>20030203.000550</version>
      |    </versions>
      |    <lastUpdated>20140623122436</lastUpdated>
      |  </versioning>
      |</metadata>
      | """.stripMargin
}
