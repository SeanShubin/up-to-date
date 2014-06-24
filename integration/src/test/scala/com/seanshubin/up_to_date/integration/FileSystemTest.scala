package com.seanshubin.up_to_date.integration

import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path, Paths}

import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class FileSystemTest extends FunSuite {
  def createFileSystem(): FileSystemImpl = {
    val charsetName: String = "utf-8"
    val charset: Charset = Charset.forName(charsetName)
    val fileSystem = new FileSystemImpl(charset)
    fileSystem
  }

  test("store and load file") {
    val fileSystem = createFileSystem()
    val fileName: String = Paths.get("target", "test-store-and-load-file.txt").toFile.getPath
    val content: String = "Hello, world!"
    fileSystem.deleteFileIfExists(fileName)

    assert(fileSystem.fileExists(fileName) === false)

    fileSystem.storeStringIntoFile(fileName, content)
    assert(fileSystem.fileExists(fileName) === true)

    val actual = fileSystem.loadFileIntoString(fileName)
    assert(content === actual)

    fileSystem.deleteFile(fileName)
    assert(fileSystem.fileExists(fileName) === false)
  }

  test("walk file tree") {
    val fileSystem = createFileSystem()
    val baseDir = Paths.get("target", "test-find-pom")
    val samplePomFile = baseDir.resolve("pom.xml").toString
    fileSystem.createDirectories(baseDir)
    fileSystem.storeStringIntoFile(samplePomFile, "<xml/>")
    val found = new ArrayBuffer[Path]()
    fileSystem.walkFileTree(baseDir, new FileVisitor[Path] {
      override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = ???

      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        found.append(file)
        FileVisitResult.CONTINUE
      }

      override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = FileVisitResult.CONTINUE

      override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
    })
    assert(found.map(_.toString).contains(samplePomFile.toString))
  }
}
