package com.seanshubin.up_to_date.logic

import java.io.{InputStream, Reader}
import java.nio.file.{FileVisitor, Path}

trait FileSystem {
  def fileExists(path: Path): Boolean

  def ensureDirectoriesExist(path: Path)

  def loadString(path: Path): String

  def storeString(path: Path, content: String)

  def pathToInputStream(path: Path): InputStream

  def pathToReader(path: Path): Reader

  def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path])

  def lastModified(path: Path): Long

  def dataInputFor(path: Path): DataInputStreamWrapper

  def dataOutputFor(path: Path): DataOutputStreamWrapper
}
