package com.seanshubin.up_to_date.integration

import java.io.IOException
import java.nio.charset.{StandardCharsets, Charset}
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file._

import org.scalatest.{FunSuite, Matchers}

import scala.collection.mutable.ArrayBuffer

class FileSystemTest extends FunSuite with Matchers {
  val charset = StandardCharsets.UTF_8

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
    deleteFileIfExists(file)

    assert(fileSystem.fileExists(file) === false)

    storeStringIntoFile(file, content)
    assert(fileSystem.fileExists(file) === true)

    val actual = fileSystem.loadFileIntoString(file)
    assert(content === actual)

    deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

  test("walk file tree") {
    val fileSystem = createFileSystem()
    val baseDir = Paths.get("target", "test-find-pom")
    val samplePomFile = baseDir.resolve("pom.xml")
    fileSystem.ensureDirectoriesExist(baseDir)
    storeStringIntoFile(samplePomFile, "<xml/>")
    val found = new ArrayBuffer[Path]()
    fileSystem.walkFileTree(baseDir, new FileVisitor[Path] {
      override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = ???

      override def visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult = {
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
    deleteFileIfExists(file)

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

    deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

  test("last modified") {
    val fileSystem = createFileSystem()
    val file: Path = Paths.get("target", "test-last-modified.txt")
    val content: String = "Hello, world!"
    deleteFileIfExists(file)

    assert(fileSystem.fileExists(file) === false)

    val beforeCreateSeconds = System.currentTimeMillis() / 1000
    storeStringIntoFile(file, content)
    val afterCreateSeconds = System.currentTimeMillis() / 1000
    assert(fileSystem.fileExists(file) === true)

    val lastModifiedSeconds = fileSystem.lastModified(file) / 1000
    lastModifiedSeconds should be >= beforeCreateSeconds
    lastModifiedSeconds should be <= afterCreateSeconds

    deleteFile(file)
    assert(fileSystem.fileExists(file) === false)
  }

  def storeStringIntoFile(path: Path, content: String): Unit = storeStringToPath(content, path, charset)

  def deleteFile(path: Path): Unit = Files.delete(path)

  def deleteFileIfExists(path: Path): Unit = Files.deleteIfExists(path)

  def storeStringToPath(s: String, path: Path, charset: Charset): Unit = Files.write(path, stringToBytes(s, charset))

  def stringToBytes(s: String, charset: Charset): Array[Byte] = s.getBytes(charset)
}
