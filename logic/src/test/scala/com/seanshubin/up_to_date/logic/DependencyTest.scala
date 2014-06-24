package com.seanshubin.up_to_date.logic

import org.scalatest.{FunSuite, ShouldMatchers}

class DependencyTest extends FunSuite with ShouldMatchers {
  test("create") {
    val dependency = Dependency(".", "org.codehaus.groovy", "groovy-eclipse-compiler", "2.7.0-01")
    assert(dependency.id === "org.codehaus.groovy.groovy-eclipse-compiler")
  }

  test("create when version in artifact id") {
    val dependency = Dependency(".", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", "2.4.0-rc2")
    assert(dependency.id === "com.fasterxml.jackson.module.jackson-module-scala")
  }

  test("simple version comparison") {
    val later = Dependency(".", "joda-time", "joda-time", "2.3")
    val earlier = Dependency(".", "joda-time", "joda-time", "1.4")
    earlier should be < later
  }
}
