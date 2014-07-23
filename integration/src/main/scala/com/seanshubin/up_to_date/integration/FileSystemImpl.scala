package com.seanshubin.up_to_date.integration

import java.io.{InputStream, Reader}
import java.nio.charset.Charset
import java.nio.file.{FileVisitor, Files, Path}

import com.seanshubin.up_to_date.logic._

class FileSystemImpl(charset: Charset) extends FileSystem {
  override def fileExists(path: Path): Boolean = Files.exists(path)

  override def ensureDirectoriesExist(path: Path): Unit = Files.createDirectories(path)

  override def loadString(path: Path): String = new String(Files.readAllBytes(path), charset)

  override def storeString(path: Path, content: String): Unit = Files.write(path, content.getBytes(charset))

  override def pathToInputStream(path: Path): InputStream = Files.newInputStream(path)

  override def pathToReader(path: Path): Reader = Files.newBufferedReader(path, charset)

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = Files.walkFileTree(start, visitor)

  override def lastModified(path: Path): Long = Files.getLastModifiedTime(path).toMillis

  override def dataInputFor(path: Path): DataInputStreamWrapper = new DataInputStreamWrapperImpl(path)

  override def dataOutputFor(path: Path): DataOutputStreamWrapper = new DataOutputStreamWrapperImpl(path)
}
