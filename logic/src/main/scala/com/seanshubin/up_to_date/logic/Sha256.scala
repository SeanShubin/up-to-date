package com.seanshubin.up_to_date.logic

import java.security.MessageDigest
import java.nio.charset.Charset

class Sha256(charsetName: String) extends OneWayHash {
  private val messageDigest = MessageDigest.getInstance("sha-256")
  private val charset: Charset = Charset.forName(charsetName)

  override def toHexString(source: String): String = {
    val bytes = source.getBytes(charset)
    val digest = messageDigest.digest(bytes)
    val hexCharArray = digest.flatMap(OneWayHash.byteToHexString)
    val hexString = new String(hexCharArray)
    hexString
  }
}
