package com.seanshubin.uptodate.logic

import com.seanshubin.uptodate.logic.Datum.{DatumInt, DatumUtf}

import scala.collection.mutable.ArrayBuffer

class DataOutputStreamWrapperStub extends DataOutputStreamWrapperNotImplemented{
  val written:ArrayBuffer[Datum] = new ArrayBuffer[Datum]()
  var closed = false
  override def writeInt(value: Int): Unit = {
    if(closed) throw new RuntimeException("not allowed after closed")
    written.append(DatumInt(value))
  }

  override def writeUTF(value: String): Unit = {
    if(closed) throw new RuntimeException("not allowed after closed")
    written.append(DatumUtf(value))
  }

  override def close(): Unit = {
    if(closed) throw new RuntimeException("closed twice")
    closed = true
  }
}
