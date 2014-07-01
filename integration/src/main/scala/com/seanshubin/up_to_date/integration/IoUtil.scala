package com.seanshubin.up_to_date.integration

import java.io.{InputStream, InputStreamReader, Reader, Writer}
import java.nio.charset.Charset

import scala.annotation.tailrec

object IoUtil {
  def inputStreamToString(inputStream: InputStream, charset: Charset): String = readerToString(inputStreamReader(inputStream, charset))

  def inputStreamReader(inputStream: InputStream, charset: Charset): Reader = new InputStreamReader(inputStream, charset)

  def readerToString(reader: Reader): String = readerToString(reader, new StringBuilder())

  @tailrec
  def readerToString(reader: Reader, stringBuilder: StringBuilder): String = {
    val currentChar = reader.read()
    if (currentChar == -1) {
      stringBuilder.toString()
    } else {
      stringBuilder.append(currentChar.asInstanceOf[Char])
      readerToString(reader, stringBuilder)
    }
  }

  def stringToWriter(s: String, writer: Writer) {
    writer.write(s)
    writer.flush()
  }
}
