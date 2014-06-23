package com.seanshubin.up_to_date.logic

class PomFileScannerImpl(pomFileFinder: PomFileFinder, pomParser: PomParser) extends PomFileScanner {
  override def scanExistingDependencies(): ExistingDependencies = {
    val pomFiles = pomFileFinder.relevantPomFiles()
    val dependencies = pomFiles.flatMap(pomParser.parseDependencies)
    val existingDependencies = ExistingDependencies(dependencies)
    existingDependencies
  }
}
