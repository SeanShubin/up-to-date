package com.seanshubin.up_to_date.integration

import java.io.{DataInput, DataInputStream, InputStream}
import java.nio.file.{Files, Path}

import com.seanshubin.up_to_date.logic.DataInputStreamWrapper

class DataInputStreamWrapperImpl(path: Path) extends DataInputStreamWrapper {
  val inputStream: InputStream = Files.newInputStream(path)
  val dataInput: DataInput = new DataInputStream(inputStream)

  override def readInt(): Int = dataInput.readInt()

  override def readUTF(): String = dataInput.readUTF()

  override def close(): Unit = inputStream.close()
}
