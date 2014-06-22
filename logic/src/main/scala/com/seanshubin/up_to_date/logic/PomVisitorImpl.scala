package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitResult, Path}
import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes

class PomVisitorImpl(name: String, excludeDirectories: Seq[String], found: String => Unit) extends PomVisitor {
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
