package com.seanshubin.up_to_date.logic

import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

import scala.collection.mutable.ArrayBuffer

class PomFileFinderTest extends FunSuite with EasyMockSugar {
  val stubAttributes: BasicFileAttributes = null
  val stubFile: Path = null
  val stubException: IOException = null

  test("trigger found if matches") {
    val directory = Paths.get("blah")
    val file = directory.resolve("pom.xml")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.visitFile(file, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, Seq(directory), "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Seq(file))
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("don't normally skip directories") {
    val baseDirectory = Paths.get("blah")
    val importantDirectory = baseDirectory.resolve("important")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.preVisitDirectory(importantDirectory, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, Seq(baseDirectory), "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Seq())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("skip excluded directories") {
    val baseDirectory = Paths.get("blah")
    val targetDirectory: Path = baseDirectory.resolve("target")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.preVisitDirectory(targetDirectory, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, Seq(baseDirectory), "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Seq())
    assert(visitResults === Seq(FileVisitResult.SKIP_SUBTREE))
  }

  test("do nothing if visit file failed") {
    val baseDirectory = Paths.get("blah")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.visitFileFailed(stubFile, stubException))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, Seq(baseDirectory), "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Seq())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("don't trigger found if does not match") {
    val baseDirectory = Paths.get("blah")
    val notPom = baseDirectory.resolve("not-pom.xml")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.visitFile(notPom, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, Seq(baseDirectory), "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Seq())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("do nothing after visiting directory") {
    val baseDirectory = Paths.get("blah")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.postVisitDirectory(stubFile, stubException))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, Seq(baseDirectory), "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Seq())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }
}
