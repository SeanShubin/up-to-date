package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class PropertyUtilTest extends FunSuite {
  test("combine property maps") {
    val a = Map("a" -> "b", "c" -> "d", "h" -> "i")
    val b = Map("e" -> "f", "c" -> "g", "h" -> "i")
    val combineResult = PropertyUtil.combine(a, b)
    assert(combineResult.merged === Map("a" -> "b", "c" -> "d", "e" -> "f", "h" -> "i"))
    assert(combineResult.conflict === Map("c" -> Set("d", "g")))
  }
  test("expand property maps") {
    val properties = Map(
      "abcdef" -> "${ab}.${cdef}",
      "ab" -> "a.b",
      "cdef" -> "c.d.${ef}",
      "ef" -> "e.f")
    val expanded = PropertyUtil.expand("prefix-${abcdef}-suffix", properties)
    assert(expanded === "prefix-a.b.c.d.e.f-suffix")
  }
}
