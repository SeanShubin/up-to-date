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

  test("get newline separator") {
    assert(StringUtil.getNewlineSeparator("name", "aaa", "\n") === "\n")
    assert(StringUtil.getNewlineSeparator("name", "aaa\rbbb", "\n") === "\r")
    assert(StringUtil.getNewlineSeparator("name", "aaa\r\nbbb", "\n") === "\r\n")
    assert(StringUtil.getNewlineSeparator("name", "aaa\nbbb", "\n") === "\n")
    assert(StringUtil.getNewlineSeparator("name", "aaa\rbbb\rccc", "\n") === "\r")
    assert(StringUtil.getNewlineSeparator("name", "aaa\r\nbbb\r\nccc", "\n") === "\r\n")
    assert(StringUtil.getNewlineSeparator("name", "aaa\nbbb\nccc", "\n") === "\n")
  }

  test("fail if inconsistent newline separator") {
    val expectedMessage = "Inconsistent newline separator in name"
    val exception = intercept[RuntimeException] {
      StringUtil.getNewlineSeparator("name", "aaa\r\nbbb\nccc", "\n")
    }
    assert(exception.getMessage === expectedMessage)
  }
}
