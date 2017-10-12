package com.seanshubin.uptodate.logic

trait DataOutputStreamWrapper {
  def writeInt(value: Int)

  def writeUTF(value: String)

  def close()
}
