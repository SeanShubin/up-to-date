package com.seanshubin.up_to_date.console

import java.nio.charset.Charset

import com.seanshubin.up_to_date.integration.{FileSystemImpl, HttpImpl, SystemClockImpl}
import com.seanshubin.up_to_date.logic._

trait ProductionRunnerWiring {
  def configuration: ValidConfiguration

  lazy val charsetName: String = "utf-8"
  lazy val charset: Charset = Charset.forName(charsetName)
  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charset)
  lazy val pomFileFinder: PomFileFinder = new PomFileFinderImpl(
    fileSystem, configuration.pomFileName, configuration.directoryNamesToSkip)
  lazy val pomParser: PomParser = new PomParserImpl(fileSystem)
  lazy val pomFileScanner: PomFileScanner = new PomFileScannerImpl(pomFileFinder, pomParser)
  lazy val httpDelegate: Http = new HttpImpl(charset, notifications)
  lazy val oneWayHash: OneWayHash = new Sha256(charset)
  lazy val http: Http = new HttpCache(
    httpDelegate, oneWayHash, fileSystem, configuration.cacheDirectory, configuration.cacheExpireMilliseconds, systemClock, notifications)
  lazy val metadataParser: MetadataParser = new MetadataParserImpl(charset)
  lazy val mavenRepositoryScanner: MavenRepositoryScanner = new MavenRepositoryScannerImpl(
    configuration.mavenRepositories, http, metadataParser)
  lazy val dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
  lazy val upgrader: Upgrader = new UpgraderImpl
  lazy val reporter: Reporter = new ReporterImpl
  lazy val notifications: Notifications = new LineEmittingNotifications(systemClock, emitLine)
  lazy val runner: RunnerImpl = new RunnerImpl(
    pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter, notifications)
}

object ProductionRunnerWiring {
  def apply(theValidConfiguration: ValidConfiguration) = new ProductionRunnerWiring {
    override def configuration: ValidConfiguration = theValidConfiguration
  }
}
