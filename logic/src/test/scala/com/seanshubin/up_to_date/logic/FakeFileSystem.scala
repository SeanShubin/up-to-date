package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path}

import org.w3c.dom.Document

abstract class FakeFileSystem extends FileSystem {
  override def fileExists(path: Path): Boolean = ???

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = ???

  override def loadFileIntoDocument(path: Path): Document = ???

  override def loadFileIntoString(path: Path): String = ???

  override def dataInputFor(path: Path): DataInputStreamWrapper = ???

  override def ensureDirectoriesExist(path: Path): Unit = ???

  override def dataOutputFor(path: Path): DataOutputStreamWrapper = ???

  override def lastModifiedSeconds(path: Path): Long = ???
}
