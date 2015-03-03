package com.seanshubin.up_to_date.logic

import java.nio.file.Path

import com.seanshubin.devon.core.devon.DevonMarshaller

import scala.reflect.runtime.universe

class FileSystemReportGeneratorImpl(reportPath: Path, fileSystem: FileSystem, devonMarshaller: DevonMarshaller) extends FileSystemReportGenerator {
  override def sendReportToFileSystem[T: universe.TypeTag](report: T, reportName: String): Unit = {
    val devonReport = devonMarshaller.valueToPretty(report)
    fileSystem.ensureDirectoriesExist(reportPath)
    fileSystem.storeLines(reportPath.resolve(reportName + ".txt"), devonReport)
  }
}
