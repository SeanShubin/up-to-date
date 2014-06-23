package com.seanshubin.up_to_date.integration

import java.io.File
import java.nio.file.{FileVisitor, Path, Paths}

import com.seanshubin.up_to_date.logic.PomVisitorImpl
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class FileSystemTest extends FunSuite {
  test("store and load file") {
    val fileName: String = Paths.get("target", "test-store-and-load-file.txt").toFile.getPath
    val charsetName: String = "utf-8"
    val content: String = "Hello, world!"
    val fileSystem = new FileSystemImpl(charsetName)
    fileSystem.deleteFileIfExists(fileName)

    assert(fileSystem.fileExists(fileName) === false)

    fileSystem.storeStringIntoFile(fileName, content)
    assert(fileSystem.fileExists(fileName) === true)

    val actual = fileSystem.loadFileIntoString(fileName)
    assert(content === actual)

    fileSystem.deleteFile(fileName)
    assert(fileSystem.fileExists(fileName) === false)
  }

  test("find pom files") {
    val charsetName: String = "utf-8"
    val fileSystem = new FileSystemImpl(charsetName)
    val baseDir = Paths.get("target", "test-find-pom")
    val samplePomFile = baseDir.resolve("pom.xml").toString
    fileSystem.createDirectories(baseDir)
    fileSystem.storeStringIntoFile(samplePomFile, "<xml/>")
    val actual = new ArrayBuffer[String]()
    def found(pomName: String) = actual.append(pomName)
    val pomVisitor: FileVisitor[Path] = new PomVisitorImpl("pom.xml", Seq(), found)
    val expected = Seq("target" + File.separator + "test-find-pom" + File.separator + "pom.xml")
    fileSystem.visit(baseDir, pomVisitor)
    assert(actual === expected)
  }
}
