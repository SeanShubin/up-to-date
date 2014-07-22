package com.seanshubin.up_to_date.logic

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

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
    val sampleDocumentInputStream = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8))
    val fileSystem = mock[FileSystem]
    val pomParser = new PomParserImpl(fileSystem)
    val path = Paths.get("foo", "bar", "pom.xml")
    val expectedDependency1 = PomDependency("org.scala-lang", "scala-library", "2.11.1")
    val expectedDependency2 = PomDependency("joda-time", "joda-time", "2.3")
    val expected = path.toString -> Seq(expectedDependency1, expectedDependency2)
    expecting {
      fileSystem.pathToInputStream(path).andReturn(sampleDocumentInputStream)
    }
    whenExecuting(fileSystem) {
      val actual = pomParser.parseDependencies(path)
      assert(actual === expected)
    }
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
    val sampleDocumentInputStream = new ByteArrayInputStream(sampleData.getBytes(StandardCharsets.UTF_8))
    val fileSystem = mock[FileSystem]
    val pomParser = new PomParserImpl(fileSystem)
    val path = Paths.get("foo", "bar", "pom.xml")
    val expectedDependency = PomDependency("ddd", "eee", "fff")
    val expected = path.toString -> Seq(expectedDependency)
    expecting {
      fileSystem.pathToInputStream(path).andReturn(sampleDocumentInputStream)
    }
    whenExecuting(fileSystem) {
      val actual = pomParser.parseDependencies(path)
      assert(actual === expected)
    }
  }
}
