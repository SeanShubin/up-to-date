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
  val dependency1: PomDependency = PomDependency("org.scala-lang", "scala-library", "2.11.1")
  val dependency2: PomDependency = PomDependency("joda-time", "joda-time", "2.3")
  val existingDependencies: ExistingDependencies = ExistingDependencies(Map("pom.xml" -> Seq(dependency1, dependency2)))
  val dependencyVersions: DependencyVersions = DependencyVersions(Map(
    GroupAndArtifact("org.scala-lang", "scala-library") -> LocationAndVersions("repo1", Set("1", "2", "3")),
    GroupAndArtifact("joda-time", "joda-time") -> LocationAndVersions("repo2", Set("4", "5", "6"))
  ))
  val pomReport =
    """{
      |  "pom.xml" : [ {
      |    "group" : "org.scala-lang",
      |    "artifact" : "scala-library",
      |    "version" : "2.11.1"
      |  }, {
      |    "group" : "joda-time",
      |    "artifact" : "joda-time",
      |    "version" : "2.3"
      |  } ]
      |}""".stripMargin
  val repositoryReport =
    """{
      |  "GroupAndArtifact(org.scala-lang,scala-library)" : {
      |    "location" : "repo1",
      |    "versions" : [ "1", "2", "3" ]
      |  },
      |  "GroupAndArtifact(joda-time,joda-time)" : {
      |    "location" : "repo2",
      |    "versions" : [ "4", "5", "6" ]
      |  }
      |}""".stripMargin
  val recommendationsReport =
    """{
      |  "GroupAndArtifact(com.fasterxml.jackson.module,jackson-module-scala_2.11)" : {
      |    "bestAvailable" : "1.3.0",
      |    "repositoryLocation" : "http://repo1/jackson",
      |    "byPomLocation" : {
      |      "pom.xml" : {
      |        "fromVersion" : "1.2.3",
      |        "maybeToVersion" : "1.3.0"
      |      },
      |      "logic/pom.xml" : {
      |        "fromVersion" : "1.3-rc1",
      |        "maybeToVersion" : "1.3.0"
      |      },
      |      "integration/pom.xml" : {
      |        "fromVersion" : "1.4-rc1"
      |      }
      |    }
      |  },
      |  "GroupAndArtifact(org.scala-lang,scala-library)" : {
      |    "bestAvailable" : "2.11.1",
      |    "repositoryLocation" : "http://repo1/scala",
      |    "byPomLocation" : {
      |      "pom.xml" : {
      |        "fromVersion" : "2.11.1"
      |      },
      |      "logic/pom.xml" : {
      |        "fromVersion" : "2.10",
      |        "maybeToVersion" : "2.11.1"
      |      },
      |      "integration/pom.xml" : {
      |        "fromVersion" : "2.11.0",
      |        "maybeToVersion" : "2.11.1"
      |      }
      |    }
      |  }
      |}""".stripMargin
  val inconsistencyReport =
    """{
      |  "GroupAndArtifact(com.fasterxml.jackson.module,jackson-module-scala_2.11)" : {
      |    "bestAvailable" : "1.3.0",
      |    "repositoryLocation" : "http://repo1/jackson",
      |    "byPomLocation" : {
      |      "pom.xml" : {
      |        "fromVersion" : "1.2.3",
      |        "maybeToVersion" : "1.3.0"
      |      },
      |      "logic/pom.xml" : {
      |        "fromVersion" : "1.3-rc1",
      |        "maybeToVersion" : "1.3.0"
      |      },
      |      "integration/pom.xml" : {
      |        "fromVersion" : "1.4-rc1"
      |      }
      |    }
      |  },
      |  "GroupAndArtifact(org.scala-lang,scala-library)" : {
      |    "bestAvailable" : "2.11.1",
      |    "repositoryLocation" : "http://repo1/scala",
      |    "byPomLocation" : {
      |      "pom.xml" : {
      |        "fromVersion" : "2.11.1"
      |      },
      |      "logic/pom.xml" : {
      |        "fromVersion" : "2.10",
      |        "maybeToVersion" : "2.11.1"
      |      },
      |      "integration/pom.xml" : {
      |        "fromVersion" : "2.11.0",
      |        "maybeToVersion" : "2.11.1"
      |      }
      |    }
      |  }
      |}""".stripMargin
  val jacksonId = GroupAndArtifact("com.fasterxml.jackson.module", "jackson-module-scala_2.11")
  val scalaId = GroupAndArtifact("org.scala-lang", "scala-library")
  val jodaId = GroupAndArtifact("joda-time", "joda-time")
  val recommendations = Recommendations(
    byGroupAndArtifact = Map(
      jacksonId ->
        RecommendationBySource(bestAvailable = "1.3.0", repositoryLocation = "http://repo1/jackson", Map(
          "pom.xml" -> RecommendedVersionBump("1.2.3", Some("1.3.0")),
          "logic/pom.xml" -> RecommendedVersionBump("1.3-rc1", Some("1.3.0")),
          "integration/pom.xml" -> RecommendedVersionBump("1.4-rc1", None))),
      scalaId ->
        RecommendationBySource(bestAvailable = "2.11.1", repositoryLocation = "http://repo1/scala", Map(
          "pom.xml" -> RecommendedVersionBump("2.11.1", None),
          "logic/pom.xml" -> RecommendedVersionBump("2.10", Some("2.11.1")),
          "integration/pom.xml" -> RecommendedVersionBump("2.11.0", Some("2.11.1")))),
      jodaId ->
        RecommendationBySource(bestAvailable = "2.3", repositoryLocation = "http://repo2/joda", Map(
          "pom.xml" -> RecommendedVersionBump("2.3", None),
          "logic/pom.xml" -> RecommendedVersionBump("2.3", None),
          "integration/pom.xml" -> RecommendedVersionBump("2.3", None)))))

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
