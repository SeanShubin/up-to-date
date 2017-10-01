package com.seanshubin.up_to_date.console

import java.nio.charset.Charset

import com.seanshubin.devon.domain.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.up_to_date.integration.{FileSystemImpl, HttpImpl, SystemClockImpl}
import com.seanshubin.up_to_date.logic.DurationFormat.MillisecondsFormat
import com.seanshubin.up_to_date.logic._

trait ApplicationDependencyInjection {
  def configuration: Configuration

  lazy val reportNames: ReportNames = ReportNames(
    pom = "pom",
    repository = "repository",
    inconsistency = "inconsistency",
    upgradesToApply = "apply",
    upgradesToIgnore = "ignore",
    statusQuo = "status-quo",
    notFound = "not-found",
    byDependency = "by-dependency",
    summary = "summary",
    unexpandedPom = "unexpanded-pom",
    propertyConflict = "property-conflict"
  )
  lazy val charsetName: String = "utf-8"
  lazy val charset: Charset = Charset.forName(charsetName)
  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charset)
  lazy val pomFileFinder: PomFileFinder = new PomFileFinderImpl(
    fileSystem, configuration.directoriesToSearch, configuration.pomFileName, configuration.directoryNamesToSkip)
  lazy val pomParser: PomParser = new PomParserImpl(charset)
  lazy val pomFileScanner: PomFileScanner = new PomFileScannerImpl(
    pomFileFinder, pomParser, fileSystem)
  lazy val httpDelegate: Http = new HttpImpl(charset, notifications)
  lazy val oneWayHash: OneWayHash = new Sha256(charset)
  lazy val http: Http = new HttpCache(
    httpDelegate, oneWayHash, fileSystem, configuration.cacheDirectory, MillisecondsFormat.parse(configuration.cacheExpire),
    systemClock, notifications)
  lazy val metadataParser: MetadataParser = new MetadataParserImpl(charset)
  lazy val mavenRepositoryScanner: MavenRepositoryScanner = new MavenRepositoryScannerImpl(
    configuration.mavenRepositories, http, metadataParser, notifications)
  lazy val dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
  lazy val pomXmlUpgrader: PomXmlUpgrader = new PomXmlUpgraderImpl(charset)
  lazy val upgrader: PomFileUpgrader = new PomFileUpgraderImpl(
    fileSystem, pomXmlUpgrader, configuration.automaticallyUpgrade)
  lazy val devonMarshaller: DevonMarshaller = DevonMarshallerWiring.Default
  lazy val reportGenerator: FileSystemReportGenerator = new FileSystemReportGeneratorImpl(
    configuration.reportDirectory, fileSystem, devonMarshaller)
  lazy val reporter: Reporter = new ReporterImpl(
    configuration.reportDirectory, reportNames, reportGenerator)
  lazy val notifications: Notifications = new LineEmittingNotifications(systemClock, devonMarshaller, emitLine)
  lazy val runner: Runnable = new RunnerImpl(
    pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, configuration.doNotUpgradeFrom,
    configuration.doNotUpgradeTo, upgrader, reporter, notifications)
}

object ApplicationDependencyInjection {
  def createRunner: Configuration => Runnable = theValidConfiguration => new ApplicationDependencyInjection {
    override def configuration: Configuration = theValidConfiguration
  }.runner
}
