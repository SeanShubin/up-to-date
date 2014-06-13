package com.seanshubin.up_to_date.logic

object SampleData {
  val configurationJsonComplete = ConfigurationJson(
    pomDirectories = Seq("pom directory 1", "pom directory 2"),
    mavenRepositories = Seq("maven repository 1", "maven repository 2"),
    automaticallyUpdate = Seq(Seq("group 1", "artifact 1", "version 1"), Seq("group 2", "artifact 2", "version 2")),
    ignore = Seq(Seq("group 3", "artifact 3", "version 3"), Seq("group 4", "artifact 4", "version 4")),
    reportDirectory = Some("report directory"),
    cacheDirectory = Some("cache directory"),
    cacheExpire = Some("5 days"))
  val validConfiguration = ValidConfiguration(
    pomDirectories = Seq("pom directory 1", "pom directory 2"),
    mavenRepositories = Seq("maven repository 1", "maven repository 2"),
    automaticallyUpdate = Seq(Seq("group 1", "artifact 1", "version 1"), Seq("group 2", "artifact 2", "version 2")),
    ignore = Seq(Seq("group 3", "artifact 3", "version 3"), Seq("group 4", "artifact 4", "version 4")),
    reportDirectory = "report directory",
    cacheDirectory = "cache directory",
    cacheExpireMilliseconds = DurationFormat.MillisecondsFormat.parse("5 days")
  )
}
