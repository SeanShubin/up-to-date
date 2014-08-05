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
}
