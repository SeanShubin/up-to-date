package com.seanshubin.up_to_date.logic

import org.scalatest.{FunSuite, ShouldMatchers}

class DependencyTest extends FunSuite with ShouldMatchers {
  test("create") {
    val actual = Dependency.create("org.codehaus.groovy", "groovy-eclipse-compiler", "2.7.0-01")
    val expected = Dependency("org.codehaus.groovy.groovy-eclipse-compiler", "org.codehaus.groovy", "groovy-eclipse-compiler", "2.7.0-01")
    assert(expected === actual)
  }

  test("create when version in artifact id") {
    val actual = Dependency.create("com.fasterxml.jackson.module", "jackson-module-scala_2.11", "2.4.0-rc2")
    val expected = Dependency("com.fasterxml.jackson.module.jackson-module-scala", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", "2.4.0-rc2")
    assert(expected === actual)
  }

  test("simple version comparison") {
    val later = Dependency.create("joda-time", "joda-time", "2.3")
    val earlier = Dependency.create("joda-time", "joda-time", "1.4")
    earlier should be < later
  }
}
