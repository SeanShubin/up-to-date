package com.seanshubin.uptodate.integration

import java.io.{DataOutput, DataOutputStream, OutputStream}
import java.nio.file.{Files, Path}

import com.seanshubin.uptodate.logic.DataOutputStreamWrapper

class DataOutputStreamWrapperImpl(path: Path) extends DataOutputStreamWrapper {
  val outputStream: OutputStream = Files.newOutputStream(path)
  val dataOutput: DataOutput = new DataOutputStream(outputStream)

  override def writeInt(value: Int): Unit = dataOutput.writeInt(value)

  override def writeUTF(value: String): Unit = dataOutput.writeUTF(value)

  override def close(): Unit = outputStream.close()
}
