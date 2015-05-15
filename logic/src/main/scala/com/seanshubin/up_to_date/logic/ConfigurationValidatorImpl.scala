package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import com.seanshubin.devon.core.devon.DevonMarshaller

class ConfigurationValidatorImpl(fileSystem: FileSystem,
                                 devonMarshaller: DevonMarshaller) extends ConfigurationValidator {
  private val sample = new Configuration(
    pomFileName = "pom.xml",
    directoryNamesToSkip = Set("target"),
    directoriesToSearch = Seq(Paths.get(".")),
    mavenRepositories = Seq(
      "http://repo.maven.apache.org/maven2",
      "http://onejar-maven-plugin.googlecode.com/svn/mavenrepo",
      "http://oss.sonatype.org/content/groups/scala-tools"),
    doNotUpgradeFrom = Set(GroupAndArtifact("groupIdToIgnore", "artifactIdToIgnore")),
    doNotUpgradeTo = Set(GroupArtifactVersion("groupIdToIgnore", "artifactIdToIgnore", "1.2.3")),
    automaticallyUpgrade = true,
    reportDirectory = Paths.get("generated", "sample", "report"),
    cacheDirectory = Paths.get("generated", "cache"),
    cacheExpire = "5 days"
  )

  override def validate(commandLineArguments: Seq[String]): Either[Seq[String], Configuration] = {
    if (commandLineArguments.size < 1) {
      Left(Seq("at least one command line argument required"))
    } else if (commandLineArguments.size > 1) {
      Left(Seq("no more than one command line argument allowed"))
    } else {
      validateFile(Paths.get(commandLineArguments(0)))
    }
  }

  private def validateFile(path: Path): Either[Seq[String], Configuration] = {
    if (fileSystem.fileExists(path)) {
      val text = fileSystem.loadString(path)
      validateText(path, text)
    } else {
      Left(Seq(s"file '$path' does not exist"))
    }
  }

  private def validateText(path: Path, text: String): Either[Seq[String], Configuration] = {
    try {
      val devon = devonMarshaller.fromString(text)
      val configuration = devonMarshaller.toValue(devon, classOf[Configuration])
      Right(configuration)
    } catch {
      case ex: Exception =>
        val sampleDevon = devonMarshaller.fromValue(sample)
        val sampleConfigString = devonMarshaller.toPretty(sampleDevon)
        Left(Seq(
          s"Unable to read json from '$path': ${ex.getMessage}",
          "A valid configuration might look something like this:") ++
          sampleConfigString
        )
    }
  }
}
