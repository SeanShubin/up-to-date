package com.seanshubin.up_to_date.logic

import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

import scala.collection.mutable.ArrayBuffer

class PomFileFinderImpl(fileSystem: FileSystem,
                        directoriesToSearch: Seq[Path],
                        pomFileName: String,
                        excludedDirectories: Seq[String]) extends PomFileFinder {
  override def relevantPomFiles(): Set[Path] = {
    val relevantPomFilesSeq = directoriesToSearch.flatMap(relevantPomFilesFromPath)
    relevantPomFilesSeq.toSet
  }

  private def relevantPomFilesFromPath(path: Path): Seq[Path] = {
    val files = new ArrayBuffer[Path]()
    fileSystem.walkFileTree(path, new FileVisitor[Path] {
      override def preVisitDirectory(directory: Path, attributes: BasicFileAttributes): FileVisitResult = {
        if (excludedDirectories.contains(directory.toFile.getName)) FileVisitResult.SKIP_SUBTREE
        else FileVisitResult.CONTINUE
      }

      override def visitFileFailed(file: Path, exception: IOException): FileVisitResult = FileVisitResult.CONTINUE

      override def visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult = {
        if (pomFileName == file.toFile.getName) files.append(file)
        FileVisitResult.CONTINUE
      }

      override def postVisitDirectory(directory: Path, exception: IOException): FileVisitResult = FileVisitResult.CONTINUE
    })
    files
  }
}
