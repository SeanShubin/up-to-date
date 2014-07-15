package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite

class ReporterTest extends FunSuite {
  test("observations report") {
    var actualPath: Path = null
    var actualContent: String = null
    var actualReportDirectory: Path = null
    val reportPath = Paths.get("foo")
    val observationReportName = "observations"
    val recommendationReportName = "recommendations"
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
    val reporter = new ReporterImpl(reportPath, observationReportName, recommendationReportName, fileSystem, jsonMarshaller)
    reporter.reportObservations(SampleData.existingDependencies, SampleData.dependencyVersions)
    assert(actualPath === reportPath.resolve(observationReportName))
    assert(actualContent === SampleData.observationsReport)
    assert(actualReportDirectory === Paths.get("foo"))
  }

  test("recommendations report") {
    var actualPath: Path = null
    var actualContent: String = null
    var actualReportDirectory: Path = null
    val reportPath = Paths.get("foo")
    val observationReportName = "observations"
    val recommendationReportName = "recommendations"
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
    val reporter = new ReporterImpl(reportPath, observationReportName, recommendationReportName, fileSystem, jsonMarshaller)
    reporter.reportRecommendations(SampleData.recommendations)
    assert(actualPath === reportPath.resolve(recommendationReportName))
    assert(actualContent === SampleData.recommendationsReport)
    assert(actualReportDirectory === Paths.get("foo"))
  }
}
