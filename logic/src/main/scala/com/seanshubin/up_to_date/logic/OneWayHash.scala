package com.seanshubin.up_to_date.logic

trait OneWayHash {
  def toHexString(source: String): String
}

object OneWayHash {
  def byteToHexString(b: Byte): String = f"$b%02x"

  def byteArrayToHexString(bytes: Array[Byte]): String = new String(bytes.flatMap(OneWayHash.byteToHexString))
}
