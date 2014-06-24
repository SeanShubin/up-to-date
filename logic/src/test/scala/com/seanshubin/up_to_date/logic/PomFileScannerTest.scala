package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar


class PomFileScannerTest extends FunSuite with EasyMockSugar {
  test("scan pom files for dependencies") {
    val pomFileFinder = mock[PomFileFinder]
    val pomParser = mock[PomParser]
    val pomFileScanner = new PomFileScannerImpl(pomFileFinder, pomParser)
    val samplePom1 = Paths.get("foo", "pom.xml")
    val samplePom2 = Paths.get("bar", "pom.xml")
    val samplePomFiles = Seq(samplePom1, samplePom2)
    val sampleDependencies1 = Seq(
      Dependency("group 1", "artifact 1", "version 1"),
      Dependency("group 2", "artifact 2", "version 2"))
    val sampleDependencies2 = Seq(
      Dependency("group 3", "artifact 3", "version 3"),
      Dependency("group 4", "artifact 4", "version 4"))
    val expected = ExistingDependencies(sampleDependencies1 ++ sampleDependencies2)

    expecting {
      pomFileFinder.relevantPomFiles().andReturn(samplePomFiles)
      pomParser.parseDependencies(samplePom1).andReturn(sampleDependencies1)
      pomParser.parseDependencies(samplePom2).andReturn(sampleDependencies2)
    }

    whenExecuting(pomFileFinder, pomParser) {
      val actual = pomFileScanner.scanExistingDependencies()
      assert(actual === expected)
    }
  }
}
