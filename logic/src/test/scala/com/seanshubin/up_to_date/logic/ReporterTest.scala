package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  test("sample report") {
    var actualPath: Path = null
    var actualContent: String = null
    var actualReportDirectory: Path = null
    val observationPath = Paths.get("foo", "observations")
    val fileSystem = new FakeFileSystem {
      override def storeString(path: Path, content: String): Unit = {
        actualPath = path
        actualContent = content
      }

      override def ensureDirectoriesExist(path: Path): Unit = {
        actualReportDirectory = path
      }
    }
    val jsonMarshaller = new JsonMarshallerImpl
    val reporter = new ReporterImpl(observationPath, fileSystem, jsonMarshaller)
    reporter.reportObservations(SampleData.existingDependencies, SampleData.dependencyVersions)
    assert(actualPath === observationPath)
    assert(actualContent === SampleData.observationsReport)
    assert(actualReportDirectory === Paths.get("foo"))
  }
}
