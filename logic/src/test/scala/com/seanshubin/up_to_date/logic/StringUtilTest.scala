package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class StringUtilTest extends FunSuite {
  test("escape") {
    assert(StringUtil.escape("nothing special") === "nothing special")
    assert(StringUtil.escape("  \n\b\t\f\r\"\'\\  ") === "  \\n\\b\\t\\f\\r\\\"\\\'\\\\  ")
  }

  test("double quote") {
    assert(StringUtil.doubleQuote("nothing special") === "\"nothing special\"")
    assert(StringUtil.doubleQuote("  \n\b\t\f\r\"\'\\  ") === "\"  \\n\\b\\t\\f\\r\\\"\\\'\\\\  \"")
  }
}
