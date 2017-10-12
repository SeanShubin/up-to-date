package com.seanshubin.uptodate.logic

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class PomParserTest extends FunSuite {
  test("parse dependencies") {
    val sampleData =
      """<xml>
        |    <properties>
        |        <foo>bar</foo>
        |    </properties>
        |    <dependency>
        |        <groupId>org.scala-lang</groupId>
        |        <artifactId>scala-library</artifactId>
        |        <version>2.11.1</version>
        |    </dependency>
        |    <dependency>
        |        <groupId>joda-time</groupId>
        |        <artifactId>joda-time</artifactId>
        |        <version>2.3</version>
        |    </dependency>
        |</xml>""".stripMargin
    val properties = Map(
      "foo" -> "bar"
    )
    val charset = StandardCharsets.UTF_8
    val pomParser = new PomParserImpl(charset)
    val pomName = "foo/bar/pom.xml"
    val expectedDependency1 = Dependency(pomName, "org.scala-lang", "scala-library", "2.11.1")
    val expectedDependency2 = Dependency(pomName, "joda-time", "joda-time", "2.3")
    val expected = Pom(pomName, Seq(expectedDependency1, expectedDependency2), properties)
    val actual = pomParser.parseDependencies(pomName, sampleData)
    assert(actual === expected)
  }
}
