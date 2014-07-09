package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class ConfigurationValidatorTest extends FunSuite with EasyMockSugar {
  val stubFileSystem: FileSystem = null
  val stubJsonMarshaller: JsonMarshaller = null
  val fakeFile: Path = Paths.get("file name")

  test("at least one command line argument required") {
    val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(stubFileSystem, stubJsonMarshaller)
    val actual = configurationValidator.validate(Seq())
    val expected = Left(Seq("at least one command line argument required"))
    assert(actual === expected)
  }

  test("no more than one command line argument allowed") {
    val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(stubFileSystem, stubJsonMarshaller)
    val actual = configurationValidator.validate(Seq("too", "many"))
    val expected = Left(Seq("no more than one command line argument allowed"))
    assert(actual === expected)
  }

  test("configuration file must exist") {
    val fileSystem: FileSystem = mock[FileSystem]
    val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(fileSystem, stubJsonMarshaller)

    expecting {
      fileSystem.fileExists(fakeFile).andReturn(false)
    }

    whenExecuting(fileSystem) {
      val actual = configurationValidator.validate(Seq("file name"))
      val expected = Left(Seq(s"file 'file name' does not exist"))
      assert(actual === expected)
    }
  }

  test("json must be in the correct shape") {
    val fileSystem: FileSystem = mock[FileSystem]
    val jsonMarshaller: JsonMarshaller = mock[JsonMarshaller]
    val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(fileSystem, jsonMarshaller)
    val malformedJsonString = "{  what! "
    val jsonError = new RuntimeException("Malformed json string")
    val sampleConfigString = "some sample config"

    expecting {
      fileSystem.fileExists(fakeFile).andReturn(true)
      fileSystem.loadString(fakeFile).andReturn(malformedJsonString)
      jsonMarshaller.fromJson(malformedJsonString, classOf[ConfigurationJson]).andThrow(jsonError)
      jsonMarshaller.toJson(ConfigurationJson.sample).andReturn(sampleConfigString)
    }

    whenExecuting(fileSystem, jsonMarshaller) {
      val actual = configurationValidator.validate(Seq("file name"))
      val expected = Left(Seq(
        s"Unable to read json from 'file name': Malformed json string",
        "A valid configuration might look something like this:",
        sampleConfigString))
      assert(actual === expected)
    }
  }

  test("well formed json does not meet validation rules") {
    val fileSystem: FileSystem = mock[FileSystem]
    val jsonMarshaller: JsonMarshaller = mock[JsonMarshaller]
    val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(fileSystem, jsonMarshaller)
    val jsonString = "some json"
    val parsedFromJson = SampleData.configurationJsonComplete.copy(reportDirectory = None)

    expecting {
      fileSystem.fileExists(fakeFile).andReturn(true)
      fileSystem.loadString(fakeFile).andReturn(jsonString)
      jsonMarshaller.fromJson(jsonString, classOf[ConfigurationJson]).andReturn(parsedFromJson)
    }

    whenExecuting(fileSystem, jsonMarshaller) {
      val actual = configurationValidator.validate(Seq("file name"))
      val expected = Left(List("reportDirectory is required"))
      assert(actual === expected)
    }
  }

  test("well formed json meets validation rules") {
    val fileSystem: FileSystem = mock[FileSystem]
    val jsonMarshaller: JsonMarshaller = mock[JsonMarshaller]
    val configurationValidator: ConfigurationValidator = new ConfigurationValidatorImpl(fileSystem, jsonMarshaller)
    val jsonString = "some json"
    val parsedFromJson = SampleData.configurationJsonComplete

    expecting {
      fileSystem.fileExists(fakeFile).andReturn(true)
      fileSystem.loadString(fakeFile).andReturn(jsonString)
      jsonMarshaller.fromJson(jsonString, classOf[ConfigurationJson]).andReturn(parsedFromJson)
    }

    whenExecuting(fileSystem, jsonMarshaller) {
      val actual = configurationValidator.validate(Seq("file name"))
      val expected = Right(SampleData.validConfiguration)
      assert(actual === expected)
    }
  }
}
