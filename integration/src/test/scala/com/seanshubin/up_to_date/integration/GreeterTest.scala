package com.seanshubin.up_to_date.integration

import org.scalatest.FunSuite

class GreeterTest extends FunSuite {
  test("greeting") {
    val subject: Greeter = new Greeter()
    val target = "world"
    val actual = subject.sayHello(target)
    val expected = "Hello, world!"
    assert(expected === actual)
  }
}
