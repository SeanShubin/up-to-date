package com.seanshubin.uptodate.logic

trait DataInputStreamWrapper {
  def readInt(): Int

  def readUTF(): String

  def close()
}
