package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar


class PomFileScannerTest extends FunSuite with EasyMockSugar {
  test("scan pom files for dependencies") {
    val pomFileFinder = mock[PomFileFinder]
    val pomParser = mock[PomParser]
    val fileSystem = mock[FileSystem]
    val pomFileScanner = new PomFileScannerImpl(pomFileFinder, pomParser, fileSystem)
    val samplePom1 = Paths.get("foo", "pom.xml")
    val samplePom2 = Paths.get("bar", "pom.xml")
    val path1 = samplePom1.toString
    val path2 = samplePom2.toString
    val contents1 = "contents 1"
    val contents2 = "contents 2"
    val samplePomFiles = Seq(samplePom1, samplePom2)
    val sampleDependencies1 = Seq(
      Dependency(path1, "group 1", "artifact 1", "version 1"),
      Dependency(path1, "group 2", "artifact 2", "version 2"))
    val sampleDependencies2 = Seq(
      Dependency(path2, "group 3", "artifact 3", "version 3"),
      Dependency(path2, "group 4", "artifact 4", "version 4"))
    val expectedPom1 = Pom(path1, sampleDependencies1)
    val expectedPom2 = Pom(path2, sampleDependencies2)
    val expected = Seq(expectedPom1, expectedPom2)

    expecting {
      pomFileFinder.relevantPomFiles().andReturn(samplePomFiles)
      fileSystem.loadString(samplePom1).andReturn(contents1)
      pomParser.parseDependencies(path1, contents1).andReturn(expectedPom1)
      fileSystem.loadString(samplePom2).andReturn(contents2)
      pomParser.parseDependencies(path2, contents2).andReturn(expectedPom2)
    }

    whenExecuting(pomFileFinder, pomParser, fileSystem) {
      val actual = pomFileScanner.scanPomFiles()
      assert(actual === expected)
    }
  }
}
