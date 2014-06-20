package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class OneWayHashTest extends FunSuite {
  test("byte array to hex string") {
    val bytes: Array[Byte] = Array[Byte](-128, -127, -1, 0, 1, 127)
    val actual = OneWayHash.byteArrayToHexString(bytes)
    val expected = "8081ff00017f"
    assert(expected === actual)
  }

  test("same values have same hash") {
    val oneWayHash: OneWayHash = new Sha256("utf-8")
    val s1 = "foo"
    val s2 = "foo"
    val s1Hash = oneWayHash.toHexString(s1)
    val s2Hash = oneWayHash.toHexString(s2)
    assert(s1Hash === s2Hash)
  }

  test("different values have different hash") {
    val oneWayHash: OneWayHash = new Sha256("utf-8")
    val s1 = "foo"
    val s2 = "bar"
    val s1Hash = oneWayHash.toHexString(s1)
    val s2Hash = oneWayHash.toHexString(s2)
    assert(s1Hash !== s2Hash)
  }
}
