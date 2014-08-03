package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  val reportPath = Paths.get("foo")
  val pomReportName = "pom"
  val repositoryReportName = "repository"
  val upgradesReportName = "upgrades"
  val inconsistencyReportName = "inconsistencies"

  def createReporter(fileSystem: FileSystem): Reporter = {
    val jsonMarshaller = new JsonMarshallerImpl
    val reporter = new ReporterImpl(
      reportPath,
      pomReportName,
      repositoryReportName,
      upgradesReportName,
      inconsistencyReportName,
      upgradesReportName,
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
    assert(fileSystem.actualPath === reportPath.resolve(pomReportName))
    assert(fileSystem.actualContent === SampleData.pomReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("repository report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportRepository(SampleData.libraries)
    assert(fileSystem.actualPath === reportPath.resolve(repositoryReportName))
    assert(fileSystem.actualContent === SampleData.repositoryReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("upgrades report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportUpgrades(SampleData.upgrades)
    assert(fileSystem.actualPath === reportPath.resolve(upgradesReportName))
    assert(fileSystem.actualContent === SampleData.upgradesReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("inconsistency report") {
    val fileSystem = new FakeFileSystemForReporter
    val reporter = createReporter(fileSystem)
    reporter.reportInconsistencies(SampleData.inconsistencies)
    assert(fileSystem.actualPath === reportPath.resolve(inconsistencyReportName))
    assert(fileSystem.actualContent === SampleData.inconsistencyReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }
}
