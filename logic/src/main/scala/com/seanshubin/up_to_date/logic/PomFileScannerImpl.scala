package com.seanshubin.up_to_date.logic

import java.nio.file.Path

class PomFileScannerImpl(pomFileFinder: PomFileFinder,
                         pomParser: PomParser,
                         fileSystem: FileSystem,
                         substitutions:Map[String, String]) extends PomFileScanner {

  override def scanPomFiles(): Seq[Pom] = {
    val pomFiles = pomFileFinder.relevantPomFiles()
    def scanPomFile(path: Path): Pom = {
      val rawPomContents = fileSystem.loadString(path)
      val pomContents = applySubstitutions(rawPomContents, substitutions)
      pomParser.parseDependencies(path.toString, pomContents)
    }
    val poms = pomFiles.map(scanPomFile)
    poms
  }

  private def applySubstitutions(target: String, substitutions: Map[String, String]) = {
    def applySubstitution(original:String, substitution:(String, String)):String = {
      val (regex, replacement) = substitution
      original.replaceAll(regex, replacement)
    }
    substitutions.foldLeft(target)(applySubstitution)
  }
}
