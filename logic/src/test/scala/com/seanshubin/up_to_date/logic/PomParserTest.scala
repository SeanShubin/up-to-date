package com.seanshubin.up_to_date.logic

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class PomParserTest extends FunSuite {
  test("parse dependencies") {
    val sampleData =
      """<xml>
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
    val charset = StandardCharsets.UTF_8
    val pomParser = new PomParserImpl(charset)
    val pomName = "foo/bar/pom.xml"
    val expectedDependency1 = Dependency(pomName, "org.scala-lang", "scala-library", "2.11.1")
    val expectedDependency2 = Dependency(pomName, "joda-time", "joda-time", "2.3")
    val expected = Pom(pomName, Seq(expectedDependency1, expectedDependency2), properties)
    val actual = pomParser.parseDependencies(pomName, sampleData)
    assert(actual === expected)
  }

  test("ignore if group, artifact, or version starts with $") {
    val sampleData =
      """<xml>
        |    <dependency>
        |        <groupId>$aaa</groupId>
        |        <artifactId>bbb</artifactId>
        |        <version>ccc</version>
        |    </dependency>
        |    <dependency>
        |        <groupId>ddd</groupId>
        |        <artifactId>eee</artifactId>
        |        <version>fff</version>
        |    </dependency>
        |    <dependency>
        |        <groupId>ggg</groupId>
        |        <artifactId>$hhh</artifactId>
        |        <version>iii</version>
        |    </dependency>
        |    <dependency>
        |        <groupId>jjj</groupId>
        |        <artifactId>kkk</artifactId>
        |        <version>$lll</version>
        |    </dependency>
        |</xml>""".stripMargin
    val charset = StandardCharsets.UTF_8
    val pomParser = new PomParserImpl(charset)
    val pomName = "foo/bar/pom.xml"
    val expectedDependency = Dependency(pomName.toString, "ddd", "eee", "fff")
    val expected = Pom(pomName.toString, Seq(expectedDependency), properties)
    val actual = pomParser.parseDependencies(pomName, sampleData)
    assert(actual === expected)
  }
  val properties = Map(
    """'\$\{scala\.major\}'""" -> "2.11",
    """'\$\{scala\.major\.minor\}'""" -> "2.11.6",
    """'\$\{scala\.version\}'""" -> "2.11",
    """'\$\{scala\.compat\.version\}'""" -> "2.11"
  )
}
