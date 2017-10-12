package com.seanshubin.uptodate.logic

import org.scalatest.{FunSuite, Matchers}

class DependencyTest extends FunSuite with Matchers {
  test("simple version comparison") {
    val later = Dependency(".", "joda-time", "joda-time", "2.3")
    val earlier = Dependency(".", "joda-time", "joda-time", "1.4")
    earlier should be < later
  }
}
