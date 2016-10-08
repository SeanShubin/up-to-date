package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

import com.seanshubin.devon.domain.DevonMarshallerWiring
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class FileSystemReportGeneratorImplTest extends FunSuite with EasyMockSugar {
  test("generate report") {
    val reportDir = Paths.get("report", "path")
    val reportName = "foo"
    val reportPath = reportDir.resolve(reportName + ".txt")
    val fileSystem = mock[FileSystem]
    val devonMarshaller = DevonMarshallerWiring.Default
    val generator: FileSystemReportGenerator = new FileSystemReportGeneratorImpl(reportDir, fileSystem, devonMarshaller)
    val report = Point(1, 2)
    val expected = Seq(
      "{",
      "  x 1",
      "  y 2",
      "}")
    expecting {
      fileSystem.ensureDirectoriesExist(reportDir)
      fileSystem.storeLines(reportPath, expected)
    }
    whenExecuting(fileSystem) {
      generator.sendReportToFileSystem(report, reportName)
    }
  }
}
