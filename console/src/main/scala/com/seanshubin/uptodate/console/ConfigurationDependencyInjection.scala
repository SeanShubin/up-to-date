package com.seanshubin.uptodate.console

import java.nio.charset.Charset

import com.seanshubin.devon.domain.{DevonMarshaller, DevonMarshallerWiring}
import com.seanshubin.uptodate.integration.{FileSystemImpl, HttpImpl, SystemClockImpl}
import com.seanshubin.uptodate.logic.DurationFormat.MillisecondsFormat
import com.seanshubin.uptodate.logic._

trait ConfigurationDependencyInjection {
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
  lazy val flow:Flow = new FlowImpl(
    pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, configuration.doNotUpgradeFrom,
    configuration.doNotUpgradeTo, upgrader, reporter, notifications)
  lazy val runner: Runnable = new RunnerImpl(flow)
}

object ConfigurationDependencyInjection {
  def apply(theValidConfiguration:Configuration):ConfigurationDependencyInjection =new ConfigurationDependencyInjection {
    override def configuration: Configuration = theValidConfiguration
  }
  def createRunner: Configuration => Runnable = theValidConfiguration => apply(theValidConfiguration).runner
}
