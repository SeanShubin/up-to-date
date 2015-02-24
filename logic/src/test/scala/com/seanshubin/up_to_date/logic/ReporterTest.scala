package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import com.seanshubin.devon.core.devon.DefaultDevonMarshaller
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
    val devonMarshaller = new DefaultDevonMarshaller
    val reporter = new ReporterImpl(
      reportPath,
      reportNames,
      fileSystem,
      jsonMarshaller,
      devonMarshaller)
    reporter
  }

  class StubFileSystemForReporter extends StubFileSystem {
    var actualPath: Path = null
    var actualContent: String = null
    var actualReportDirectory: Path = null
    var actualDevonPath: Path = null
    var actualDevonLines: Seq[String] = null

    override def storeString(path: Path, content: String): Unit = {
      actualPath = path
      actualContent = content
    }

    override def ensureDirectoriesExist(path: Path): Unit = {
      actualReportDirectory = path
    }

    override def storeLines(path: Path, lines: Seq[String]): Unit = {
      actualDevonPath = path
      actualDevonLines = lines
    }
  }

  test("pom report") {
    val fileSystem = new StubFileSystemForReporter
    val reporter = createReporter(fileSystem)
    val poms = SampleData.poms()
    reporter.reportPom(poms)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.pom + ".json"))
    assert(fileSystem.actualContent === samplePomReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("repository report") {
    val fileSystem = new StubFileSystemForReporter
    val reporter = createReporter(fileSystem)
    val libraries = SampleData.libraries()
    reporter.reportRepository(libraries)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.repository + ".json"))
    assert(fileSystem.actualContent === sampleLibraryReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("upgrades to apply report") {
    val fileSystem = new StubFileSystemForReporter
    val reporter = createReporter(fileSystem)
    val upgrades = SampleData.upgrades(1, 3)
    reporter.reportUpgradesToApply(upgrades)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.upgradesToApply + ".json"))
    assert(fileSystem.actualContent === sampleUpgradesToApplyReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("upgrades to ignore report") {
    val fileSystem = new StubFileSystemForReporter
    val reporter = createReporter(fileSystem)
    val upgrades = SampleData.upgrades(4, 6)
    reporter.reportUpgradesToIgnore(upgrades)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.upgradesToIgnore + ".json"))
    println(fileSystem.actualContent)
    assert(fileSystem.actualContent === sampleUpgradesToIgnoreReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("inconsistency report") {
    val fileSystem = new StubFileSystemForReporter
    val reporter = createReporter(fileSystem)
    val inconsistencies = SampleData.inconsistencies()
    reporter.reportInconsistencies(inconsistencies)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.inconsistency + ".json"))
    assert(fileSystem.actualContent === sampleInconsistencyReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  test("not found report") {
    val fileSystem = new StubFileSystemForReporter
    val reporter = createReporter(fileSystem)
    val notFound = SampleData.groupAndArtifactSeq()
    reporter.reportNotFound(notFound)
    assert(fileSystem.actualPath === reportPath.resolve(reportNames.notFound + ".json"))
    assert(fileSystem.actualContent === sampleNotFoundReport)
    assert(fileSystem.actualReportDirectory === Paths.get("foo"))
  }

  private val samplePomReport =
    """{
      |  "location-1" : {
      |    "location" : "location-1",
      |    "dependencies" : [ {
      |      "location" : "location-1",
      |      "group" : "group-1",
      |      "artifact" : "artifact-1",
      |      "version" : "version-1"
      |    }, {
      |      "location" : "location-1",
      |      "group" : "group-2",
      |      "artifact" : "artifact-2",
      |      "version" : "version-2"
      |    }, {
      |      "location" : "location-1",
      |      "group" : "group-3",
      |      "artifact" : "artifact-3",
      |      "version" : "version-3"
      |    } ]
      |  },
      |  "location-2" : {
      |    "location" : "location-2",
      |    "dependencies" : [ {
      |      "location" : "location-2",
      |      "group" : "group-1",
      |      "artifact" : "artifact-1",
      |      "version" : "version-1"
      |    }, {
      |      "location" : "location-2",
      |      "group" : "group-2",
      |      "artifact" : "artifact-2",
      |      "version" : "version-2"
      |    }, {
      |      "location" : "location-2",
      |      "group" : "group-3",
      |      "artifact" : "artifact-3",
      |      "version" : "version-3"
      |    } ]
      |  },
      |  "location-3" : {
      |    "location" : "location-3",
      |    "dependencies" : [ {
      |      "location" : "location-3",
      |      "group" : "group-1",
      |      "artifact" : "artifact-1",
      |      "version" : "version-1"
      |    }, {
      |      "location" : "location-3",
      |      "group" : "group-2",
      |      "artifact" : "artifact-2",
      |      "version" : "version-2"
      |    }, {
      |      "location" : "location-3",
      |      "group" : "group-3",
      |      "artifact" : "artifact-3",
      |      "version" : "version-3"
      |    } ]
      |  }
      |}""".stripMargin

  private val sampleLibraryReport =
    """{
      |  "location-2" : [ {
      |    "location" : "location-2",
      |    "group" : "group-2",
      |    "artifact" : "artifact-2",
      |    "versions" : [ "version-1", "version-2", "version-3" ]
      |  } ],
      |  "location-1" : [ {
      |    "location" : "location-1",
      |    "group" : "group-1",
      |    "artifact" : "artifact-1",
      |    "versions" : [ "version-1", "version-2", "version-3" ]
      |  } ],
      |  "location-3" : [ {
      |    "location" : "location-3",
      |    "group" : "group-3",
      |    "artifact" : "artifact-3",
      |    "versions" : [ "version-1", "version-2", "version-3" ]
      |  } ]
      |}""".stripMargin

  private val sampleUpgradesToApplyReport =
    """{
      |  "location-2" : [ {
      |    "location" : "location-2",
      |    "group" : "group-2",
      |    "artifact" : "artifact-2",
      |    "fromVersion" : "fromVersion-2",
      |    "toVersion" : "toVersion-2"
      |  } ],
      |  "location-1" : [ {
      |    "location" : "location-1",
      |    "group" : "group-1",
      |    "artifact" : "artifact-1",
      |    "fromVersion" : "fromVersion-1",
      |    "toVersion" : "toVersion-1"
      |  } ],
      |  "location-3" : [ {
      |    "location" : "location-3",
      |    "group" : "group-3",
      |    "artifact" : "artifact-3",
      |    "fromVersion" : "fromVersion-3",
      |    "toVersion" : "toVersion-3"
      |  } ]
      |}""".stripMargin

  private val sampleUpgradesToIgnoreReport =
    """{
      |  "location-4" : [ {
      |    "location" : "location-4",
      |    "group" : "group-4",
      |    "artifact" : "artifact-4",
      |    "fromVersion" : "fromVersion-4",
      |    "toVersion" : "toVersion-4"
      |  } ],
      |  "location-6" : [ {
      |    "location" : "location-6",
      |    "group" : "group-6",
      |    "artifact" : "artifact-6",
      |    "fromVersion" : "fromVersion-6",
      |    "toVersion" : "toVersion-6"
      |  } ],
      |  "location-5" : [ {
      |    "location" : "location-5",
      |    "group" : "group-5",
      |    "artifact" : "artifact-5",
      |    "fromVersion" : "fromVersion-5",
      |    "toVersion" : "toVersion-5"
      |  } ]
      |}""".stripMargin

  private val sampleInconsistencyReport =
    """{
      |  "GroupAndArtifact(group-1,artifact-1)" : [ {
      |    "location" : "location-1",
      |    "group" : "group-1",
      |    "artifact" : "artifact-1",
      |    "version" : "version-1"
      |  }, {
      |    "location" : "location-2",
      |    "group" : "group-1",
      |    "artifact" : "artifact-1",
      |    "version" : "version-2"
      |  }, {
      |    "location" : "location-3",
      |    "group" : "group-1",
      |    "artifact" : "artifact-1",
      |    "version" : "version-3"
      |  } ],
      |  "GroupAndArtifact(group-2,artifact-2)" : [ {
      |    "location" : "location-1",
      |    "group" : "group-2",
      |    "artifact" : "artifact-2",
      |    "version" : "version-1"
      |  }, {
      |    "location" : "location-2",
      |    "group" : "group-2",
      |    "artifact" : "artifact-2",
      |    "version" : "version-2"
      |  }, {
      |    "location" : "location-3",
      |    "group" : "group-2",
      |    "artifact" : "artifact-2",
      |    "version" : "version-3"
      |  } ],
      |  "GroupAndArtifact(group-3,artifact-3)" : [ {
      |    "location" : "location-1",
      |    "group" : "group-3",
      |    "artifact" : "artifact-3",
      |    "version" : "version-1"
      |  }, {
      |    "location" : "location-2",
      |    "group" : "group-3",
      |    "artifact" : "artifact-3",
      |    "version" : "version-2"
      |  }, {
      |    "location" : "location-3",
      |    "group" : "group-3",
      |    "artifact" : "artifact-3",
      |    "version" : "version-3"
      |  } ]
      |}""".stripMargin

  private val sampleNotFoundReport =
    """[ {
      |  "group" : "group-1",
      |  "artifact" : "artifact-1"
      |}, {
      |  "group" : "group-2",
      |  "artifact" : "artifact-2"
      |}, {
      |  "group" : "group-3",
      |  "artifact" : "artifact-3"
      |} ]""".stripMargin
}
