package com.seanshubin.uptodate.logic

import scala.reflect.runtime.universe

trait FileSystemReportGenerator {
  def sendReportToFileSystem[T: universe.TypeTag](report: T, reportName: String): Unit
}
