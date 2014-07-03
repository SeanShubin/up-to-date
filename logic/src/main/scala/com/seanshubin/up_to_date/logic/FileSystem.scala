package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path}
import java.io.{Reader, InputStream}

trait FileSystem {
  def fileExists(path: Path): Boolean

  def ensureDirectoriesExist(path: Path)

  def loadFileIntoString(path: Path): String

  def pathToInputStream(path: Path): InputStream

  def pathToReader(path: Path): Reader

  def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path])

  def lastModified(path: Path): Long

  def dataInputFor(path: Path): DataInputStreamWrapper

  def dataOutputFor(path: Path): DataOutputStreamWrapper
}
