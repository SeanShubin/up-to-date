package com.seanshubin.up_to_date.logic

import java.io.Closeable

import org.scalatest.FunSuite

class ClosableTest extends FunSuite {

  class FakeClosable extends Closeable {
    var timesCalled = 0

    override def close(): Unit = timesCalled += 1
  }

  test("close after when no exception") {
    val expected = 12345
    val fakeCloseable = new FakeClosable
    def doWithClosable(closeable: Closeable): Int = {
      expected
    }
    val actual = CloseableUtil.closeAfter(fakeCloseable)(doWithClosable)
    assert(actual === expected)
    assert(fakeCloseable.timesCalled === 1)
  }
  test("close after when exception") {
    val expected = "Ouch!"
    val fakeCloseable = new FakeClosable
    def doWithClosable(closeable: Closeable): Int = {
      throw new RuntimeException(expected)
    }
    val actual: String = try {
      CloseableUtil.closeAfter(fakeCloseable)(doWithClosable)
      fail("Should have propagated exception")
    } catch {
      case ex: RuntimeException => ex.getMessage
    }
    assert(actual === expected)
    assert(fakeCloseable.timesCalled === 1)
  }
}
