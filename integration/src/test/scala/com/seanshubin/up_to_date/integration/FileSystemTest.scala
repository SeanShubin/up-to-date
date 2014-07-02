package com.seanshubin.up_to_date.integration

import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.{FileVisitResult, FileVisitor, Path, Paths}

import org.scalatest.{FunSuite, Matchers}
import org.w3c.dom.Document

import scala.collection.mutable.ArrayBuffer

class FileSystemTest extends FunSuite with Matchers {
  def createFileSystem(): FileSystemImpl = {
    val charsetName: String = "utf-8"
    val charset: Charset = Charset.forName(charsetName)
    val fileSystem = new FileSystemImpl(charset)
    fileSystem
  }

  test("store and load file") {
    val fileSystem = createFileSystem()
    val file: Path = Paths.get("target", "test-store-and-load-file.txt")
    val content: String = "Hello, world!"
    fileSystem.deleteFileIfExists(file)

    assert(fileSystem.fileExists(file) === false)

    fileSystem.storeStringIntoFile(file, content)
    assert(fileSystem.fileExists(file) === true)

    val actual = fileSystem.loadFileIntoString(file)
    assert(content === actual)

    fileSystem.deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

  test("store and load xml file") {
    val fileSystem = createFileSystem()
    val file: Path = Paths.get("target", "test-store-and-load-file.xml")
    val content: String = "<xml>aaa</xml>"
    fileSystem.deleteFileIfExists(file)

    assert(fileSystem.fileExists(file) === false)

    fileSystem.storeStringIntoFile(file, content)
    assert(fileSystem.fileExists(file) === true)

    val actual: Document = fileSystem.loadFileIntoDocument(file)
    println(actual)
    assert(actual.getDocumentElement.getNodeName === "xml")
    assert(actual.getDocumentElement.getTextContent === "aaa")

    fileSystem.deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

  test("walk file tree") {
    val fileSystem = createFileSystem()
    val baseDir = Paths.get("target", "test-find-pom")
    val samplePomFile = baseDir.resolve("pom.xml")
    fileSystem.ensureDirectoriesExist(baseDir)
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

  test("data io") {
    val fileSystem = createFileSystem()
    val file: Path = Paths.get("target", "test-data-io.txt")
    val intWritten = 12345
    val stringWritten = "Hello, world!"
    fileSystem.deleteFileIfExists(file)

    assert(fileSystem.fileExists(file) === false)

    val dataOutput = fileSystem.dataOutputFor(file)
    dataOutput.writeInt(intWritten)
    dataOutput.writeUTF(stringWritten)
    dataOutput.close()

    assert(fileSystem.fileExists(file) === true)

    val dataInput = fileSystem.dataInputFor(file)
    val intRead = dataInput.readInt()
    val stringRead = dataInput.readUTF()

    assert(intRead === intWritten)
    assert(stringRead === stringWritten)

    fileSystem.deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

  test("last modified") {
    val fileSystem = createFileSystem()
    val file: Path = Paths.get("target", "test-last-modified.txt")
    val content: String = "Hello, world!"
    fileSystem.deleteFileIfExists(file)

    assert(fileSystem.fileExists(file) === false)

    val beforeCreateSeconds = System.currentTimeMillis() / 1000
    fileSystem.storeStringIntoFile(file, content)
    val afterCreateSeconds = System.currentTimeMillis() / 1000
    assert(fileSystem.fileExists(file) === true)

    val lastModifiedSeconds = fileSystem.lastModified(file) / 1000
    lastModifiedSeconds should be >= beforeCreateSeconds
    lastModifiedSeconds should be <= afterCreateSeconds

    fileSystem.deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

}
