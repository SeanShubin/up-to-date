package com.seanshubin.up_to_date.integration

import java.nio.charset.Charset
import java.nio.file.{FileVisitor, Files, Path}

import com.seanshubin.up_to_date.conversion.Conversion
import com.seanshubin.up_to_date.logic.FileSystem
import org.w3c.dom.Document

class FileSystemImpl(charset: Charset) extends FileSystem {
  override def fileExists(path: Path): Boolean = Files.exists(path)

  override def loadFileIntoString(path: Path): String = Conversion.loadPathToString(path, charset)

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = Files.walkFileTree(start, visitor)

  override def loadFileIntoDocument(path: Path): Document = Conversion.loadPathToDocument(path)

  def storeStringIntoFile(path: Path, content: String): Unit = Conversion.storeStringToPath(content, path, charset)

  def deleteFile(path: Path): Unit = Files.delete(path)

  def deleteFileIfExists(path: Path): Unit = Files.deleteIfExists(path)

  def createDirectories(path: Path): Unit = Files.createDirectories(path)
}
