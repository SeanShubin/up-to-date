package com.seanshubin.up_to_date.logic

import java.nio.charset.Charset
import java.security.MessageDigest

class Sha256(charset: Charset) extends OneWayHash {
  private val messageDigest = MessageDigest.getInstance("sha-256")

  override def toHexString(source: String): String = {
    val bytes = source.getBytes(charset)
    val digest = messageDigest.digest(bytes)
    val hexCharArray = digest.flatMap(OneWayHash.byteToHexString)
    val hexString = new String(hexCharArray)
    hexString
  }
}
