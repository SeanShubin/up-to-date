package com.seanshubin.up_to_date.logic

import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path}

class PomVisitorImpl(name: String, excludeDirectories: Seq[String], found: String => Unit) extends FileVisitor[Path] {
  override def preVisitDirectory(directory: Path, attributes: BasicFileAttributes): FileVisitResult = {
    if (excludeDirectories.contains(directory.toFile.getName)) FileVisitResult.SKIP_SUBTREE
    else FileVisitResult.CONTINUE
  }

  override def visitFileFailed(file: Path, exception: IOException): FileVisitResult = {
    FileVisitResult.CONTINUE
  }

  override def visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult = {
    if (name == file.toFile.getName) found(file.toFile.getPath)
    FileVisitResult.CONTINUE
  }

  override def postVisitDirectory(directory: Path, exception: IOException): FileVisitResult = {
    FileVisitResult.CONTINUE
  }
}
