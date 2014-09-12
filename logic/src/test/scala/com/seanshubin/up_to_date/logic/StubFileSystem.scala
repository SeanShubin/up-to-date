package com.seanshubin.up_to_date.logic

import java.io.{InputStream, Reader}
import java.nio.file.{FileVisitor, Path}

abstract class StubFileSystem extends FileSystem {
  override def fileExists(path: Path): Boolean = ???

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = ???

  override def pathToInputStream(path: Path): InputStream = ???

  override def loadString(path: Path): String = ???

  override def storeString(path: Path, content: String): Unit = ???

  override def lastModified(path: Path): Long = ???

  override def dataInputFor(path: Path): DataInputStreamWrapper = ???

  override def pathToReader(path: Path): Reader = ???

  override def ensureDirectoriesExist(path: Path): Unit = ???

  override def dataOutputFor(path: Path): DataOutputStreamWrapper = ???
}
