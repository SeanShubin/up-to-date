package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

object SampleData {
  val configurationJsonComplete = ConfigurationJson(
    pomFileName = Some("pom.xml"),
    directoriesToSearch = Some(Seq("pom directory 1", "pom directory 2")),
    directoryNamesToSkip = Some(Seq("skip this directory")),
    mavenRepositories = Some(Seq("maven repository 1", "maven repository 2")),
    dependenciesToAutomaticallyUpdate = Some(Seq(Seq("group 1", "artifact 1", "version 1"), Seq("group 2", "artifact 2", "version 2"))),
    ignore = Some(Seq(Seq("group 3", "artifact 3", "version 3"), Seq("group 4", "artifact 4", "version 4"))),
    reportDirectory = Some("report directory"),
    cacheDirectory = Some("cache directory"),
    cacheExpire = Some("5 days"))
  val validConfiguration = ValidConfiguration(
    pomFileName = "pom.xml",
    directoriesToSearch = Seq(Paths.get("pom directory 1"), Paths.get("pom directory 2")),
    directoryNamesToSkip = Seq("skip this directory"),
    mavenRepositories = Seq("maven repository 1", "maven repository 2"),
    dependenciesToAutomaticallyUpdate = Seq(Seq("group 1", "artifact 1", "version 1"), Seq("group 2", "artifact 2", "version 2")),
    ignore = Seq(Seq("group 3", "artifact 3", "version 3"), Seq("group 4", "artifact 4", "version 4")),
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
  val observationsReport =
    """{
      |  "existingDependencies" : {
      |    "pom.xml" : [ {
      |      "group" : "org.scala-lang",
      |      "artifact" : "scala-library",
      |      "version" : "2.11.1"
      |    }, {
      |      "group" : "joda-time",
      |      "artifact" : "joda-time",
      |      "version" : "2.3"
      |    } ]
      |  },
      |  "dependencyVersions" : {
      |    "GroupAndArtifact(org.scala-lang,scala-library)" : {
      |      "location" : "repo1",
      |      "versions" : [ "1", "2", "3" ]
      |    },
      |    "GroupAndArtifact(joda-time,joda-time)" : {
      |      "location" : "repo2",
      |      "versions" : [ "4", "5", "6" ]
      |    }
      |  }
      |}""".stripMargin
  val outOfDate: OutOfDate = null
  val automaticUpgradesPerformed: AutomaticUpgradesPerformed = null
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
