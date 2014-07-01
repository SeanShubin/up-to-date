package com.seanshubin.up_to_date.logic

trait DataOutputStreamWrapper {
  def writeInt(value: Int)

  def writeUTF(value: String)

  def close()
}
