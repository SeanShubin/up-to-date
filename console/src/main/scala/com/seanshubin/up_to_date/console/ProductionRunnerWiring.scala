package com.seanshubin.up_to_date.console

import com.seanshubin.up_to_date.integration.SystemClockImpl
import com.seanshubin.up_to_date.logic.{ValidConfiguration, _}

trait ProductionRunnerWiring {
  def validConfiguration: ValidConfiguration

  lazy val systemClock: SystemClock = new SystemClockImpl
  lazy val emitLine: String => Unit = println
  lazy val pomFileScanner: PomFileScanner = new PomFileScannerImpl
  lazy val mavenRepositoryScanner: MavenRepositoryScanner = new MavenRepositoryScannerImpl
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
