package com.seanshubin.up_to_date.conversion

import java.io._
import java.nio.charset.Charset
import java.nio.file.{Files, Path}
import javax.xml.parsers.{DocumentBuilder, DocumentBuilderFactory}

import org.w3c.dom.Document
import scala.annotation.tailrec

//conversion between: byte arrays, strings, documents, paths.
object Conversion {
  def loadPathToBytes(path: Path): Array[Byte] = Files.readAllBytes(path)

  def loadPathToString(path: Path, charset: Charset) = new String(loadPathToBytes(path), charset)

  def loadPathToDocument(path: Path): Document = closeAfter(pathToInputStream(path))(inputStreamToDocument)

  def storeStringToPath(s: String, path: Path, charset: Charset): Unit = Files.write(path, stringToBytes(s, charset))

  def stringToDocument(s: String, charset: Charset): Document = closeAfter(stringToInputStream(s, charset))(inputStreamToDocument)

  def stringToBytes(s: String, charset: Charset): Array[Byte] = s.getBytes(charset)

  def inputStreamToString(inputStream: InputStream, charset: Charset): String = {
    processRemainingCharacters(new StringBuilder(), inputStreamToReader(inputStream, charset))
  }

  def stringToOutputStream(s: String, charset: Charset, outputStream: OutputStream) {
    val writer = outputStreamToWriter(outputStream, charset)
    stringToWriter(s, writer)
  }

  def inputStreamToBytes(inputStream: InputStream): Array[Byte] = {
    val outputStream = new ByteArrayOutputStream
    feedInputStreamToOutputStream(inputStream, outputStream)
    val byteArray = outputStream.toByteArray
    byteArray
  }

  def stringToWriter(s: String, writer: Writer) {
    writer.write(s)
    writer.flush()
  }

  @tailrec
  private def processRemainingCharacters(stringBuilder: StringBuilder, reader: Reader): String = {
    val currentChar = reader.read()
    if (currentChar == -1) {
      stringBuilder.toString()
    } else {
      stringBuilder.append(currentChar.asInstanceOf[Char])
      processRemainingCharacters(stringBuilder, reader)
    }
  }

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

  private def inputStreamToReader(inputStream: InputStream, charset: Charset): Reader = new InputStreamReader(inputStream, charset)

  private def outputStreamToWriter(outputStream: OutputStream, charset: Charset): Writer = new OutputStreamWriter(outputStream, charset)

  private def feedInputStreamToOutputStream(inputStream: InputStream, outputStream: OutputStream) {
    @tailrec
    def loop(byte: Int) {
      if (byte != -1) {
        outputStream.write(byte)
        loop(inputStream.read())
      }
    }
    loop(inputStream.read())
  }
}
