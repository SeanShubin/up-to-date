package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path}

import org.w3c.dom.Document

trait FileSystem {
  def fileExists(path: Path): Boolean

  def loadFileIntoString(path: Path): String

  def loadFileIntoDocument(path: Path): Document

  def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path])
}
