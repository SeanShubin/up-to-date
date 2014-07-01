package com.seanshubin.up_to_date.integration

import java.io.{Closeable, InputStream}
import java.nio.charset.Charset
import java.nio.file.{FileVisitor, Files, Path}

import com.seanshubin.up_to_date.logic.{DataInputStreamWrapper, DataOutputStreamWrapper, DocumentUtil, FileSystem}
import org.w3c.dom.Document

class FileSystemImpl(charset: Charset) extends FileSystem {
  override def fileExists(path: Path): Boolean = Files.exists(path)

  override def loadFileIntoString(path: Path): String = loadPathToString(path, charset)

  override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = Files.walkFileTree(start, visitor)

  override def loadFileIntoDocument(path: Path): Document = closeAfter(pathToInputStream(path))(DocumentUtil.inputStreamToDocument)

  override def ensureDirectoriesExist(path: Path): Unit = Files.createDirectories(path)

  override def lastModifiedSeconds(path: Path): Long = Files.getLastModifiedTime(path).toMillis / 1000

  override def dataInputFor(path: Path): DataInputStreamWrapper = new DataInputStreamWrapperImpl(path)

  override def dataOutputFor(path: Path): DataOutputStreamWrapper = new DataOutputStreamWrapperImpl(path)

  def storeStringIntoFile(path: Path, content: String): Unit = storeStringToPath(content, path, charset)

  def deleteFile(path: Path): Unit = Files.delete(path)

  def deleteFileIfExists(path: Path): Unit = Files.deleteIfExists(path)

  private def loadPathToBytes(path: Path): Array[Byte] = Files.readAllBytes(path)

  private def loadPathToString(path: Path, charset: Charset) = new String(loadPathToBytes(path), charset)

  private def storeStringToPath(s: String, path: Path, charset: Charset): Unit = Files.write(path, stringToBytes(s, charset))

  private def stringToBytes(s: String, charset: Charset): Array[Byte] = s.getBytes(charset)

  private def pathToInputStream(path: Path): InputStream = Files.newInputStream(path)

  private def closeAfter[ResultType, ClosableType <: Closeable](closable: ClosableType)(block: ClosableType => ResultType): ResultType = {
    val result = try {
      block(closable)
    } finally {
      closable.close()
    }
    result
  }
}
