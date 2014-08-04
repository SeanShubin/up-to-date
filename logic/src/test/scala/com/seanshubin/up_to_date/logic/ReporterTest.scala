package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  val reportPath = Paths.get("foo")
  val reportNames = ReportNames(
    pom = "pom",
    repository = "repository",
    upgradesToApply = "apply",
    upgradesToIgnore = "ignore",
    inconsistency = "inconsistencies",
    statusQuo = "status-quo",
    notFound = "not-found")

  def createReporter(fileSystem: FileSystem): Reporter = {
    val jsonMarshaller = new JsonMarshallerImpl
    val reporter = new ReporterImpl(
      reportPath,
      reportNames,
      fileSystem,
      jsonMarshaller)
    reporter
  }

  class FakeFileSystemForReporter extends FakeFileSystem {
    var actualPath: Path = null
    var actualContent: String = null
    var actualReportDirectory: Path = null

    override def storeString(path: Path, content: String): Unit = {
      actualPath = path
      actualContent = content
    }

    override def ensureDirectoriesExist(path: Path): Unit = {
      actualReportDirectory = path
    }
  }

  test("pom report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportPom(SampleData.poms)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.pom))
    assert(fileSystem.actualContent === SampleData.pomReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("repository report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportRepository(SampleData.libraries)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.repository))
    assert(fileSystem.actualContent === SampleData.repositoryReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("upgrades to apply report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportUpgradesToApply(SampleData.upgrades)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.upgradesToApply))
    assert(fileSystem.actualContent === SampleData.upgradesReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("upgrades to ignore report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportUpgradesToIgnore(SampleData.upgrades)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.upgradesToIgnore))
    assert(fileSystem.actualContent === SampleData.upgradesReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("inconsistency report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportInconsistencies(SampleData.inconsistencies)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.inconsistency))
    assert(fileSystem.actualContent === SampleData.inconsistencyReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }
  test("not found report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportNotFound(SampleData.notFound)
    println(fileSystem.actualContent)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.notFound))
    assert(fileSystem.actualContent === SampleData.notFoundReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }
}
