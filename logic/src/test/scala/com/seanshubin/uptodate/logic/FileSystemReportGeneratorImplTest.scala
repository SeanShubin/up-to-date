package com.seanshubin.uptodate.logic

import java.nio.file.{Path, Paths}

import com.seanshubin.devon.domain.DevonMarshallerWiring
import org.scalatest.FunSuite

class FileSystemReportGeneratorImplTest extends FunSuite {
  test("generate report") {
    // given
    val reportDir = Paths.get("report", "path")
    val reportName = "foo"
    val fileSystem = new FileSystemStub(Set("report/path"))
    val devonMarshaller = DevonMarshallerWiring.Default
    val generator: FileSystemReportGenerator = new FileSystemReportGeneratorImpl(reportDir, fileSystem, devonMarshaller)
    val report = Point(1, 2)
    val expected = Seq(
      "{",
      "  x 1",
      "  y 2",
      "}")

    // when
    generator.sendReportToFileSystem(report, reportName)

    // then
    assert(fileSystem.linesStored("report/path/foo.txt") === expected)
  }

  class FileSystemStub(val existingDirectories: Set[String]) extends FileSystemNotImplemented {
    var linesStored: Map[String, Seq[String]] = Map()

    override def ensureDirectoriesExist(path: Path): Unit = {
      existingDirectories.contains(path.toString)
    }

    override def storeLines(path: Path, lines: Seq[String]): Unit = {
      linesStored = linesStored.updated(path.toString, lines)
    }
  }

}
