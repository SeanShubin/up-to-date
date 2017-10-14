package com.seanshubin.uptodate.logic

import com.seanshubin.uptodate.logic.Datum.{DatumInt, DatumUtf}


class DataInputStreamWrapperStub(data:Datum*) extends DataInputStreamWrapperNotImplemented {
  var index = 0
  var closed = false
  override def readInt(): Int = {
    if(closed) throw new RuntimeException("not allowed after closed")
    val DatumInt(value) = data(index)
    index += 1
    value
  }

  override def readUTF(): String = {
    if(closed) throw new RuntimeException("not allowed after closed")
    val DatumUtf(value) = data(index)
    index += 1
    value
  }

  override def close(): Unit = {
    if(closed) throw new RuntimeException("closed twice")
    closed = true
  }
}
