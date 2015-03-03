package com.seanshubin.up_to_date.logic

import scala.reflect.runtime.universe

trait FileSystemReportGenerator {
  def sendReportToFileSystem[T: universe.TypeTag](report: T, reportName: String): Unit
}
