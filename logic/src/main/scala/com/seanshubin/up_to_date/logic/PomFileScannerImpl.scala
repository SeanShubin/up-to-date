package com.seanshubin.up_to_date.logic

class PomFileScannerImpl(pomFileFinder: PomFileFinder, pomParser: PomParser) extends PomFileScanner {
  override def scanPomFiles(): Seq[Pom] = {
    val pomFiles = pomFileFinder.relevantPomFiles()
    val poms = pomFiles.map(pomParser.parseDependencies)
    poms
  }
}
