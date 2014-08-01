package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

case class ConfigurationJson(pomFileName: Option[String],
                             directoriesToSearch: Option[Seq[String]],
                             directoryNamesToSkip: Option[Seq[String]],
                             mavenRepositories: Option[Seq[String]],
                             doNotUpgradeFrom: Option[Seq[Seq[String]]],
                             doNotUpgradeTo: Option[Seq[Seq[String]]],
                             automaticallyUpgrade: Option[Boolean],
                             reportDirectory: Option[String],
                             cacheDirectory: Option[String],
                             cacheExpire: Option[String]) {
  def validate(): Either[Seq[String], ValidConfiguration] = {
    val missingErrors = errorIfMissing(pomFileName, "pomFileName") ++
      errorIfMissing(directoriesToSearch, "directoriesToSearch") ++
      errorIfMissing(directoryNamesToSkip, "directoryNamesToSkip") ++
      errorIfMissing(mavenRepositories, "mavenRepositories") ++
      errorIfMissing(doNotUpgradeTo, "doNotUpgradeTo") ++
      errorIfMissing(doNotUpgradeFrom, "doNotUpgradeFrom") ++
      errorIfMissing(automaticallyUpgrade, "automaticallyUpgrade") ++
      errorIfMissing(reportDirectory, "reportDirectory") ++
      errorIfMissing(cacheDirectory, "cacheDirectory") ++
      errorIfMissing(cacheExpire, "cacheExpire")
    if (missingErrors.size == 0) {
      val ConfigurationJson(Some(thePomFileName), Some(theDirectoriesToSearch), Some(theDirectoryNamesToSkip),
      Some(theMavenRepositories), Some(theDoNotUpgradeFrom), Some(theDoNotUpgradeTo), Some(theAutomaticallyUpgrade),
      Some(theReportDirectory), Some(theCacheDirectory), Some(theCacheExpire)) = this

      val errorOrExpireMilliseconds = validateCacheExpire(theCacheExpire)
      val errorOrDoNotUpgradeFrom = validateDoNotUpgradeFrom(theDoNotUpgradeFrom)
      val errorOrDoNotUpgradeTo = validateDoNotUpgradeTo(theDoNotUpgradeTo)

      val errors =
        extractLeft(errorOrExpireMilliseconds) ++
          extractLeft(errorOrDoNotUpgradeFrom) ++
          extractLeft(errorOrDoNotUpgradeTo)

      if (errors.size == 0) {
        val Right(expireMilliseconds) = errorOrExpireMilliseconds
        val Right(doNotUpgradeFrom) = errorOrDoNotUpgradeFrom
        val Right(doNotUpgradeTo) = errorOrDoNotUpgradeTo
        Right(ValidConfiguration(
          thePomFileName,
          theDirectoriesToSearch.map(nameToPath),
          theDirectoryNamesToSkip,
          theMavenRepositories,
          doNotUpgradeFrom,
          doNotUpgradeTo,
          theAutomaticallyUpgrade,
          nameToPath(theReportDirectory),
          nameToPath(theCacheDirectory),
          expireMilliseconds))
      } else {
        Left(errors)
      }
    } else {
      Left(missingErrors)
    }
  }

  private def errorIfMissing[T](maybeValue: Option[T], message: String): Seq[String] = {
    maybeValue match {
      case Some(_) => Seq()
      case None => Seq(message + " is required")
    }
  }

  private def nameToPath(name: String): Path = Paths.get(name)

  private def validateCacheExpire(cacheExpire: String): Either[Seq[String], Long] = try {
    Right(DurationFormat.MillisecondsFormat.parse(cacheExpire))
  } catch {
    case ex: RuntimeException => Left(Seq("unable to parse milliseconds for cacheExpire: " + ex.getMessage))
  }

  private def validateDoNotUpgradeFrom(values: Seq[Seq[String]]): Either[Seq[String], Set[GroupAndArtifact]] = {
    val validated = values.map(validateGroupAndArtifact)
    val errorMessages = validated.flatMap(extractLeft)
    if (errorMessages.size == 0) {
      Right(validated.map(extractRight).toSet)
    } else {
      Left(errorMessages)
    }
  }

  private def validateGroupAndArtifact(values: Seq[String]): Either[Seq[String], GroupAndArtifact] = {
    values match {
      case Seq(group, artifact) => Right(GroupAndArtifact(group, artifact))
      case x => Left(Seq(s"Cannot convert $x to group, artifact"))
    }
  }

  private def validateDoNotUpgradeTo(values: Seq[Seq[String]]): Either[Seq[String], Set[GroupArtifactVersion]] = {
    val validated = values.map(validateGroupArtifactVersion)
    val errorMessages = validated.flatMap(extractLeft)
    if (errorMessages.size == 0) {
      Right(validated.map(extractRight).toSet)
    } else {
      Left(errorMessages)
    }
  }

  private def extractLeft[T](either: Either[Seq[String], T]): Seq[String] = either match {
    case Left(values) => values
    case Right(_) => Seq()
  }

  private def extractRight[T](either: Either[Seq[String], T]): T = either match {
    case Left(_) => throw new RuntimeException("logic error, don't extract right unless you are sure it is valid")
    case Right(value) => value
  }

  private def validateGroupArtifactVersion(values: Seq[String]): Either[Seq[String], GroupArtifactVersion] = {
    values match {
      case Seq(group, artifact, version) => Right(GroupArtifactVersion(group, artifact, version))
      case x => Left(Seq(s"Cannot convert $x to group, artifact, version"))
    }
  }

  private def toGroupAndArtifact(values: Seq[String]): GroupAndArtifact = {
    values match {
      case Seq(group, artifact) => GroupAndArtifact(group, artifact)
      case x => throw new RuntimeException("Cannot convert $x to group and artifact")
    }
  }
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
    doNotUpgradeFrom = Some(Seq(Seq("groupIdToIgnore", "artifactIdToIgnore", "1.2.3"))),
    doNotUpgradeTo = Some(Seq(Seq("groupIdToIgnore", "artifactIdToIgnore", "1.2.3"))),
    automaticallyUpgrade = Some(true),
    reportDirectory = Some("generated/sample/report"),
    cacheDirectory = Some("generated/cache"),
    cacheExpire = Some("5 days")
  )
}
