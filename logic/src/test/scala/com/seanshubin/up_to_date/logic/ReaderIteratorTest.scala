package com.seanshubin.up_to_date.logic

import java.io.StringReader

import org.scalatest.FunSuite

class ReaderIteratorTest extends FunSuite {
  test("reader to iterator") {
    val expected = "Hello, world!"
    val reader = new StringReader(expected)
    val iterator = new ReaderIterator(reader)
    val actual = iterator.toSeq.mkString
    assert(actual === expected)
  }
}
