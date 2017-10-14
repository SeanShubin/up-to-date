package com.seanshubin.uptodate.logic

trait DataInputStreamWrapperNotImplemented extends DataInputStreamWrapper{
  override def readInt(): Int = ???

  override def readUTF(): String = ???

  override def close(): Unit = ???
}
