package com.seanshubin.up_to_date.logic

import java.nio.file.{FileVisitor, Path, Paths, FileVisitResult}
import org.scalatest.FunSuite
import java.nio.file.attribute.BasicFileAttributes
import java.io.{File, IOException}
import scala.collection.mutable.ArrayBuffer

class PomVisitorTest extends FunSuite {

  class Helper {
    val results = new ArrayBuffer[String]()

    def found(file: String) = results.append(file)

    val pomVisitor: FileVisitor[Path] = new PomVisitorImpl("pom.xml", Seq("target"), found)
    val stubAttributes: BasicFileAttributes = null
    val stubFile: Path = null
    val stubException: IOException = null
  }

  test("don't normally skip directories") {
    val helper = new Helper()
    val actual = helper.pomVisitor.preVisitDirectory(Paths.get("blah", "important"), helper.stubAttributes)
    val expected = FileVisitResult.CONTINUE
    assert(actual === expected)
    assert(helper.results === Seq())
  }

  test("skip excluded directories") {
    val helper = new Helper()
    val actual = helper.pomVisitor.preVisitDirectory(Paths.get("blah", "target"), helper.stubAttributes)
    val expected = FileVisitResult.SKIP_SUBTREE
    assert(actual === expected)
    assert(helper.results === Seq())
  }

  test("do nothing if visit file failed") {
    val helper = new Helper()
    val actual = helper.pomVisitor.visitFileFailed(helper.stubFile, helper.stubException)
    val expected = FileVisitResult.CONTINUE
    assert(actual === expected)
    assert(helper.results === Seq())
  }

  test("trigger found if matches") {
    val helper = new Helper()
    val actual = helper.pomVisitor.visitFile(Paths.get("blah", "pom.xml"), helper.stubAttributes)
    val expected = FileVisitResult.CONTINUE
    assert(actual === expected)
    assert(helper.results === Seq("blah" + File.separator + "pom.xml"))
  }

  test("don't trigger found if does not match") {
    val helper = new Helper()
    val actual = helper.pomVisitor.visitFile(Paths.get("blah", "not-pom.xml"), helper.stubAttributes)
    val expected = FileVisitResult.CONTINUE
    assert(actual === expected)
    assert(helper.results === Seq())
  }

  test("do nothing after visiting directory") {
    val helper = new Helper()
    val actual = helper.pomVisitor.postVisitDirectory(helper.stubFile, helper.stubException)
    val expected = FileVisitResult.CONTINUE
    assert(actual === expected)
    assert(helper.results === Seq())
  }
}
