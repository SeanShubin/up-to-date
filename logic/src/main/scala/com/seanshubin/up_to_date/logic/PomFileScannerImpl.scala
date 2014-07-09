package com.seanshubin.up_to_date.logic

class PomFileScannerImpl(pomFileFinder: PomFileFinder, pomParser: PomParser) extends PomFileScanner {
  override def scanExistingDependencies(): ExistingDependencies = {
    val pomFiles = pomFileFinder.relevantPomFiles()
    val dependencies = pomFiles.map(pomParser.parseDependencies).toMap
    val existingDependencies = ExistingDependencies(dependencies)
    existingDependencies
  }
}
