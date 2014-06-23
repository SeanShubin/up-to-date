package com.seanshubin.up_to_date.integration

import java.nio.charset.Charset
import java.nio.file.{FileVisitor, Path, Files, Paths}

import com.seanshubin.up_to_date.logic.FileSystem

class FileSystemImpl(charsetName: String) extends FileSystem {
  private val charset = Charset.forName(charsetName)

  override def fileExists(fileName: String): Boolean = {
    Files.exists(Paths.get(fileName))
  }

  override def loadFileIntoString(fileName: String): String = {
    new String(Files.readAllBytes(Paths.get(fileName)), charset)
  }

  override def visit(start: Path, visitor: FileVisitor[Path]): Unit = {
    Files.walkFileTree(start, visitor)
  }

  def storeStringIntoFile(fileName: String, content: String): Unit = {
    Files.write(Paths.get(fileName), content.getBytes(charset))
  }

  def deleteFile(fileName: String): Unit = {
    Files.delete(Paths.get(fileName))
  }

  def deleteFileIfExists(fileName: String): Unit = {
    Files.deleteIfExists(Paths.get(fileName))
  }

  def createDirectories(path: Path): Unit = {
    Files.createDirectories(path)
  }
}
