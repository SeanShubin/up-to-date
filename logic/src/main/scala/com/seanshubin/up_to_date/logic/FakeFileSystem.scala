package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path}

import java.io.{Reader, InputStream}

abstract class FakeFileSystem extends FileSystem {
  override def fileExists(path: Path): Boolean = ???

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = ???

  override def pathToInputStream(path: Path): InputStream = ???

  override def loadFileIntoString(path: Path): String = ???

  override def lastModified(path: Path): Long = ???

  override def dataInputFor(path: Path): DataInputStreamWrapper = ???

  override def pathToReader(path: Path): Reader = ???

  override def ensureDirectoriesExist(path: Path): Unit = ???

  override def dataOutputFor(path: Path): DataOutputStreamWrapper = ???
}
