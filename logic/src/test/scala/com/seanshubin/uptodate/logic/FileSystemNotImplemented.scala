package com.seanshubin.uptodate.logic

import java.io.{InputStream, Reader}
import java.nio.file.{FileVisitor, Path}

trait FileSystemNotImplemented extends FileSystem {
  override def fileExists(path: Path): Boolean = ???

  override def ensureDirectoriesExist(path: Path): Unit = ???

  override def loadString(path: Path): String = ???

  override def storeString(path: Path, content: String): Unit = ???

  override def storeLines(path: Path, lines: Seq[String]): Unit = ???

  override def pathToInputStream(path: Path): InputStream = ???

  override def pathToReader(path: Path): Reader = ???

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = ???

  override def lastModified(path: Path): Long = ???

  override def dataInputFor(path: Path): DataInputStreamWrapper = ???

  override def dataOutputFor(path: Path): DataOutputStreamWrapper = ???
}
