package com.seanshubin.up_to_date.logic

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import com.seanshubin.up_to_date.conversion.Conversion
import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class PomParserTest extends FunSuite with EasyMockSugar {
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
    val sampleDocument = Conversion.stringToDocument(sampleData, StandardCharsets.UTF_8)
    val fileSystem = mock[FileSystem]
    val pomParser = new PomParserImpl(fileSystem)
    val path = Paths.get("foo", "bar", "pom.xml")
    val expectedDependency1 = Dependency(path.toString, "org.scala-lang", "scala-library", "2.11.1")
    val expectedDependency2 = Dependency(path.toString, "joda-time", "joda-time", "2.3")
    val expected = Seq(expectedDependency1, expectedDependency2)
    expecting {
      fileSystem.loadFileIntoDocument(path).andReturn(sampleDocument)
    }
    whenExecuting(fileSystem) {
      val actual = pomParser.parseDependencies(path)
      assert(actual === expected)
    }
  }
}
