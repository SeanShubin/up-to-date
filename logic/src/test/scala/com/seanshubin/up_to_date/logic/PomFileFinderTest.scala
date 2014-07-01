package com.seanshubin.up_to_date.logic

import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar
import org.w3c.dom.Document

import scala.collection.mutable.ArrayBuffer

class PomFileFinderTest extends FunSuite with EasyMockSugar {
  val stubAttributes: BasicFileAttributes = null
  val stubFile: Path = null
  val stubException: IOException = null

  abstract class FakeFileSystem extends FileSystem {
    override def fileExists(path: Path): Boolean = ???

    override def ensureDirectoriesExist(path: Path): Unit = ???

    override def loadFileIntoString(path: Path): String = ???

    override def loadFileIntoDocument(path: Path): Document = ???

    override def lastModified(path: Path): Long = ???

    override def dataInputFor(path: Path): DataInputStreamWrapper = ???

    override def dataOutputFor(path: Path): DataOutputStreamWrapper = ???
  }

  test("trigger found if matches") {
    val file = Paths.get("blah", "pom.xml")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.visitFile(file, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Set(file))
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("don't normally skip directories") {
    val importantDirectory = Paths.get("blah", "important")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.preVisitDirectory(importantDirectory, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Set())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("skip excluded directories") {
    val targetDirectory: Path = Paths.get("blah", "target")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.preVisitDirectory(targetDirectory, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Set())
    assert(visitResults === Seq(FileVisitResult.SKIP_SUBTREE))
  }

  test("do nothing if visit file failed") {
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.visitFileFailed(stubFile, stubException))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Set())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("don't trigger found if does not match") {
    val notPom = Paths.get("blah", "not-pom.xml")
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.visitFile(notPom, stubAttributes))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Set())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }

  test("do nothing after visiting directory") {
    val visitResults = new ArrayBuffer[FileVisitResult]()
    val fileTreeWalker = new FakeFileSystem {
      override def walkFileTree(start: Path, visitor: FileVisitor[_ >: Path]): Unit = {
        visitResults.append(visitor.postVisitDirectory(stubFile, stubException))
      }
    }
    val finder: PomFileFinder = new PomFileFinderImpl(fileTreeWalker, "pom.xml", Seq("target"))
    val found = finder.relevantPomFiles()
    assert(found === Set())
    assert(visitResults === Seq(FileVisitResult.CONTINUE))
  }
}
