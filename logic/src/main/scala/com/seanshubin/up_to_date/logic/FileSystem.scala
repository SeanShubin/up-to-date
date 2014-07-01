package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path}

import org.w3c.dom.Document

trait FileSystem {
  def fileExists(path: Path): Boolean

  def ensureDirectoriesExist(path: Path)

  def loadFileIntoString(path: Path): String

  def loadFileIntoDocument(path: Path): Document

  def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path])

  def lastModifiedSeconds(path: Path): Long

  def dataInputFor(path: Path): DataInputStreamWrapper

  def dataOutputFor(path: Path): DataOutputStreamWrapper
}
