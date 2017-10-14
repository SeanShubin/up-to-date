package com.seanshubin.uptodate.logic

trait DataOutputStreamWrapperNotImplemented extends DataOutputStreamWrapper{
  override def writeInt(value: Int): Unit = ???

  override def writeUTF(value: String): Unit = ???

  override def close(): Unit = ???
}
