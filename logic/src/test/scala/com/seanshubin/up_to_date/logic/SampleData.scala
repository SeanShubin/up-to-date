package com.seanshubin.up_to_date.logic

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
    directoriesToSearch = Seq("pom directory 1", "pom directory 2"),
    directoryNamesToSkip = Seq("skip this directory"),
    mavenRepositories = Seq("maven repository 1", "maven repository 2"),
    dependenciesToAutomaticallyUpdate = Seq(Seq("group 1", "artifact 1", "version 1"), Seq("group 2", "artifact 2", "version 2")),
    ignore = Seq(Seq("group 3", "artifact 3", "version 3"), Seq("group 4", "artifact 4", "version 4")),
    reportDirectory = "report directory",
    cacheDirectory = "cache directory",
    cacheExpireMilliseconds = DurationFormat.MillisecondsFormat.parse("5 days")
  )
  val dependency1: Dependency = Dependency(".", "org.scala-lang", "scala-library", "2.11.1")
  val dependency2: Dependency = Dependency(".", "joda-time", "joda-time", "2.3")
  val existingDependencies: ExistingDependencies = ExistingDependencies(Set(dependency1, dependency2))
  val latestDependencies: DependencyVersions = null
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
