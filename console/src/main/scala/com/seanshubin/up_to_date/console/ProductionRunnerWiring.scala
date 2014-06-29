package com.seanshubin.up_to_date.console

import java.nio.charset.Charset

import com.seanshubin.up_to_date.integration.{FileSystemImpl, SystemClockImpl}
import com.seanshubin.up_to_date.logic._

trait ProductionRunnerWiring {
  def validConfiguration: ValidConfiguration

  lazy val charsetName: String = "utf-8"
  lazy val charset: Charset = Charset.forName(charsetName)
  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val emitLine: String => Unit = println
  lazy val fileSystem: FileSystem = new FileSystemImpl(charset)
  lazy val pomFileFinder: PomFileFinder = new PomFileFinderImpl(
    fileSystem, validConfiguration.pomFileName, validConfiguration.directoryNamesToSkip)
  lazy val pomParser: PomParser = new PomParserImpl(fileSystem)
  lazy val pomFileScanner: PomFileScanner = new PomFileScannerImpl(pomFileFinder, pomParser)
  lazy val http: Http = ???
  lazy val metadataParser: MetadataParser = ???
  lazy val mavenRepositoryScanner: MavenRepositoryScanner = new MavenRepositoryScannerImpl(
    validConfiguration.mavenRepositories, http, metadataParser)
  lazy val dependencyUpgradeAnalyzer: DependencyUpgradeAnalyzer = new DependencyUpgradeAnalyzerImpl
  lazy val upgrader: Upgrader = new UpgraderImpl
  lazy val reporter: Reporter = new ReporterImpl
  lazy val notifications: Notifications = new LineEmittingNotifications(emitLine)
  lazy val runner: RunnerImpl = new RunnerImpl(
    systemClock, pomFileScanner, mavenRepositoryScanner, dependencyUpgradeAnalyzer, upgrader, reporter, notifications)
}

object ProductionRunnerWiring {
  def apply(theValidConfiguration: ValidConfiguration) = new ProductionRunnerWiring {
    override def validConfiguration: ValidConfiguration = theValidConfiguration
  }
}
