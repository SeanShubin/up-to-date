package com.seanshubin.up_to_date.logic

trait DataInputStreamWrapper {
  def readInt(): Int

  def readUTF(): String

  def close()
}
