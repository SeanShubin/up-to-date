package com.seanshubin.up_to_date.logic

import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar
import java.io.ByteArrayInputStream

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
    val sampleDocumentInputStream = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8))
    val fileSystem = mock[FileSystem]
    val pomParser = new PomParserImpl(fileSystem)
    val path = Paths.get("foo", "bar", "pom.xml")
    val expectedDependency1 = Dependency(path.toString, "org.scala-lang", "scala-library", "2.11.1")
    val expectedDependency2 = Dependency(path.toString, "joda-time", "joda-time", "2.3")
    val expected = Set(expectedDependency1, expectedDependency2)
    expecting {
      fileSystem.pathToInputStream(path).andReturn(sampleDocumentInputStream)
    }
    whenExecuting(fileSystem) {
      val actual = pomParser.parseDependencies(path)
      assert(actual === expected)
    }
  }
}
