package com.seanshubin.up_to_date.console

import java.nio.charset.Charset

import com.seanshubin.up_to_date.integration.{FileSystemImpl, HttpImpl, SystemClockImpl}
import com.seanshubin.up_to_date.logic._

trait ProductionRunnerWiring {
  def configuration: ValidConfiguration

  lazy val reportNames: ReportNames = ReportNames(
    pom = "pom.json",
    repository = "repository.json",
    inconsistency = "inconsistency.json",
    upgradesToApply = "apply.json",
    upgradesToIgnore = "ignore.json",
    statusQuo = "status-quo.json",
    notFound = "not-found.json"
  )
  lazy val charsetName: String = "utf-8"
  lazy val charset: Charset = Charset.forName(charsetName)
  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charset)
  lazy val pomFileFinder: PomFileFinder = new PomFileFinderImpl(
    fileSystem, configuration.directoriesToSearch, configuration.pomFileName, configuration.directoryNamesToSkip)
  lazy val pomParser: PomParser = new PomParserImpl(charset)
  lazy val pomFileScanner: PomFileScanner = new PomFileScannerImpl(pomFileFinder, pomParser, fileSystem)
  lazy val httpDelegate: Http = new HttpImpl(charset, notifications)
  lazy val oneWayHash: OneWayHash = new Sha256(charset)
  lazy val http: Http = new HttpCache(
    httpDelegate, oneWayHash, fileSystem, configuration.cacheDirectory, configuration.cacheExpireMilliseconds,
    systemClock, notifications)
  lazy val metadataParser: MetadataParser = new MetadataParserImpl(charset)
  lazy val mavenRepositoryScanner: MavenRepositoryScanner = new MavenRepositoryScannerImpl(
    configuration.mavenRepositories, http, metadataParser)
  lazy val dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
  lazy val pomXmlUpgrader: PomXmlUpgrader = new PomXmlUpgraderImpl(charset)
  lazy val upgrader: PomFileUpgrader = new PomFileUpgraderImpl(
    fileSystem, pomXmlUpgrader, configuration.automaticallyUpgrade)
  lazy val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl
  lazy val reporter: Reporter = new ReporterImpl(
    configuration.reportDirectory, reportNames, fileSystem, jsonMarshaller)
  lazy val notifications: Notifications = new LineEmittingNotifications(systemClock, emitLine)
  lazy val runner: Runner = new RunnerImpl(
    pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, configuration.doNotUpgradeFrom,
    configuration.doNotUpgradeTo, upgrader, reporter, notifications)
}

object ProductionRunnerWiring {
  def apply(theValidConfiguration: ValidConfiguration) = new ProductionRunnerWiring {
    override def configuration: ValidConfiguration = theValidConfiguration
  }
}
