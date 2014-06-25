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
  val existingDependencies: ExistingDependencies = null
  val latestDependencies: LatestDependencies = null
  val outOfDate: OutOfDate = null
  val automaticUpgradesPerformed: AutomaticUpgradesPerformed = null
}
