package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ReporterTest extends FunSuite with EasyMockSugar {
  test("pom report") {
    new Helper {
      lazy val poms = SampleData.poms()

      override def expecting = () => {
        fileSystemReportGenerator.sendReportToFileSystem(Pom.groupByLocation(poms), reportNames.pom)
      }

      override def whenExecuting = () => {
        reporter.reportPom(poms)
      }
    }
  }

  test("repository report") {
    new Helper {
      lazy val libraries = SampleData.libraries()

      override def expecting = () => {
        fileSystemReportGenerator.sendReportToFileSystem(Library.groupByLocation(libraries), reportNames.repository)
      }

      override def whenExecuting = () => {
        reporter.reportRepository(libraries)
      }
    }
  }

  test("upgrades to apply report") {
    new Helper {
      lazy val upgrades = SampleData.upgrades(1, 3)

      override def expecting = () => {
        fileSystemReportGenerator.sendReportToFileSystem(Upgrade.groupByLocation(upgrades), reportNames.upgradesToApply)
      }

      override def whenExecuting = () => {
        reporter.reportUpgradesToApply(upgrades)
      }
    }
  }

  test("upgrades to ignore report") {
    new Helper {
      lazy val upgrades = SampleData.upgrades(4, 6)

      override def expecting = () => {
        fileSystemReportGenerator.sendReportToFileSystem(Upgrade.groupByLocation(upgrades), reportNames.upgradesToIgnore)
      }

      override def whenExecuting = () => {
        reporter.reportUpgradesToIgnore(upgrades)
      }
    }
  }

  test("inconsistency report") {
    new Helper {
      lazy val inconsistency = SampleData.inconsistencies()

      override def expecting = () => {
        fileSystemReportGenerator.sendReportToFileSystem(inconsistency, reportNames.inconsistency)
      }

      override def whenExecuting = () => {
        reporter.reportInconsistencies(inconsistency)
      }
    }
  }

  test("not found report") {
    new Helper {
      lazy val notFound = SampleData.groupAndArtifactSeq()

      override def expecting = () => {
        fileSystemReportGenerator.sendReportToFileSystem(notFound, reportNames.notFound)
      }

      override def whenExecuting = () => {
        reporter.reportNotFound(notFound)
      }
    }
  }

  trait Helper {
    lazy val reportPath: Path = Paths.get("report", "path")
    lazy val reportNames = ReportNames(
      pom = "pom",
      repository = "repository",
      upgradesToApply = "apply",
      upgradesToIgnore = "ignore",
      inconsistency = "inconsistencies",
      statusQuo = "status-quo",
      notFound = "not-found",
      byDependency = "by-dependency",
      summary = "summary"
    )
    lazy val fileSystemReportGenerator = mock[FileSystemReportGenerator]
    lazy val reporter = new ReporterImpl(reportPath, reportNames, fileSystemReportGenerator)

    def expecting: () => Unit

    def whenExecuting: () => Unit

    expecting()
    EasyMockSugar.whenExecuting(fileSystemReportGenerator) {
      whenExecuting()
    }
  }

}
