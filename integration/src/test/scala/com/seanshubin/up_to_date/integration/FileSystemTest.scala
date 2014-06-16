package com.seanshubin.up_to_date.integration

import java.nio.file.Paths

import org.scalatest.FunSuite

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
}
