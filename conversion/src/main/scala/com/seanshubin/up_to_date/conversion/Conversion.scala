package com.seanshubin.up_to_date.conversion

import java.io.{ByteArrayInputStream, Closeable, InputStream}
import java.nio.charset.Charset
import java.nio.file.{Files, Path}
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}

import org.w3c.dom.Document

//conversion between: byte arrays, strings, documents, paths.
object Conversion {
  def loadPathToBytes(path: Path): Array[Byte] = Files.readAllBytes(path)

  def loadPathToString(path: Path, charset: Charset) = new String(loadPathToBytes(path), charset)

  def loadPathToDocument(path: Path): Document = closeAfter(pathToInputStream(path))(inputStreamToDocument)

  def storeStringToPath(s:String, path:Path, charset:Charset): Unit = Files.write(path, stringToBytes(s, charset))

  def stringToDocument(s: String, charset: Charset): Document = closeAfter(stringToInputStream(s, charset))(inputStreamToDocument)

  def stringToBytes(s:String, charset:Charset):Array[Byte] = s.getBytes(charset)

  private val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
  private val documentBuilder: DocumentBuilder = documentBuilderFactory.newDocumentBuilder()

  private def closeAfter[ResultType, ClosableType <: Closeable](closable: ClosableType)(block: ClosableType => ResultType): ResultType = {
    val result = try {
      block(closable)
    } finally {
      closable.close()
    }
    result
  }

  private def stringToInputStream(s: String, charset: Charset): InputStream = new ByteArrayInputStream(s.getBytes(charset))

  private def pathToInputStream(path: Path): InputStream = Files.newInputStream(path)

  private def inputStreamToDocument(inputStream: InputStream): Document = documentBuilder.parse(inputStream)
}
