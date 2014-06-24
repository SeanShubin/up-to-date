package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path}

trait FileSystem {
  def fileExists(fileName: String): Boolean

  def loadFileIntoString(fileName: String): String

  def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path])
}
