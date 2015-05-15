package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class PomFileScannerImpl(pomFileFinder: PomFileFinder,
                         pomParser: PomParser,
                         fileSystem: FileSystem) extends PomFileScanner {

  override def scanPomFiles(): Seq[Pom] = {
    val pomFiles = pomFileFinder.relevantPomFiles()
    def scanPomFile(path: Path): Pom = {
      val pomContents = fileSystem.loadString(path)
      pomParser.parseDependencies(path.toString, pomContents)
    }
    val poms = pomFiles.map(scanPomFile)
    poms
  }
}
