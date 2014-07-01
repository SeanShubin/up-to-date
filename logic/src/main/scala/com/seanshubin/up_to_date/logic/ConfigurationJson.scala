package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

case class ConfigurationJson(pomFileName: Option[String],
                             directoriesToSearch: Option[Seq[String]],
                             directoryNamesToSkip: Option[Seq[String]],
                             mavenRepositories: Option[Seq[String]],
                             dependenciesToAutomaticallyUpdate: Option[Seq[Seq[String]]],
                             ignore: Option[Seq[Seq[String]]],
                             reportDirectory: Option[String],
                             cacheDirectory: Option[String],
                             cacheExpire: Option[String]) {
  def validate(): Either[Seq[String], ValidConfiguration] = {
    this match {
      case ConfigurationJson(Some(thePomFileName),
      Some(theDirectoriesToSearch),
      Some(theDirectoryNamesToSkip),
      Some(theMavenRepositories),
      Some(theAutomaticallyUpdate),
      Some(theIgnore),
      Some(theReportDirectory),
      Some(theCacheDirectory),
      Some(theCacheExpire)) =>
        try {
          val expireMilliseconds = DurationFormat.MillisecondsFormat.parse(theCacheExpire)
          Right(ValidConfiguration(
            thePomFileName,
            theDirectoriesToSearch.map(nameToPath),
            theDirectoryNamesToSkip,
            theMavenRepositories,
            theAutomaticallyUpdate,
            theIgnore,
            nameToPath(theReportDirectory),
            nameToPath(theCacheDirectory),
            expireMilliseconds))
        } catch {
          case ex: RuntimeException => Left(Seq("unable to parse milliseconds for cacheExpire: " + ex.getMessage))
        }
      case x: ConfigurationJson =>
        Left(errorIfMissing(x.pomFileName, "pomFileName") ++
          errorIfMissing(x.directoriesToSearch, "directoriesToSearch") ++
          errorIfMissing(x.directoryNamesToSkip, "directoryNamesToSkip") ++
          errorIfMissing(x.mavenRepositories, "mavenRepositories") ++
          errorIfMissing(x.dependenciesToAutomaticallyUpdate, "dependenciesToAutomaticallyUpdate") ++
          errorIfMissing(x.ignore, "ignore") ++
          errorIfMissing(x.reportDirectory, "reportDirectory") ++
          errorIfMissing(x.cacheDirectory, "cacheDirectory") ++
          errorIfMissing(x.cacheExpire, "cacheExpire"))
    }
  }

  private def errorIfMissing[T](maybeValue: Option[T], message: String): Seq[String] = {
    maybeValue match {
      case Some(_) => Seq()
      case None => Seq(message + " is required")
    }
  }

  private def nameToPath(name: String): Path = Paths.get(name)
}

object ConfigurationJson {
  val sample = new ConfigurationJson(
    pomFileName = Some("pom.xml"),
    directoryNamesToSkip = Some(Seq("target")),
    directoriesToSearch = Some(Seq(".")),
    mavenRepositories = Some(Seq(
      "http://repo.maven.apache.org/maven2",
      "http://onejar-maven-plugin.googlecode.com/svn/mavenrepo",
      "http://oss.sonatype.org/content/groups/scala-tools")),
    dependenciesToAutomaticallyUpdate = Some(Seq(Seq("org.scala-lang", "scala-library"), Seq("joda-time", "joda-time"))),
    ignore = Some(Seq(Seq("groupIdToIgnore", "artifactIdToIgnore"))),
    reportDirectory = Some("generated/sample/report"),
    cacheDirectory = Some("generated/cache"),
    cacheExpire = Some("5 days")
  )
}
