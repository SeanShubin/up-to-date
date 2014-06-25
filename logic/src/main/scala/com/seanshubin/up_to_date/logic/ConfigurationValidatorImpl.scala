package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

class ConfigurationValidatorImpl(fileSystem: FileSystem,
                                 jsonMarshaller: JsonMarshaller) extends ConfigurationValidator {
  override def validate(commandLineArguments: Seq[String]): Either[Seq[String], ValidConfiguration] = {
    if (commandLineArguments.size < 1) {
      Left(Seq("at least one command line argument required"))
    } else if (commandLineArguments.size > 1) {
      Left(Seq("no more than one command line argument allowed"))
    } else {
      validateFile(Paths.get(commandLineArguments(0)))
    }
  }

  private def validateFile(path: Path): Either[Seq[String], ValidConfiguration] = {
    if (fileSystem.fileExists(path)) {
      val json = fileSystem.loadFileIntoString(path)
      validateJson(json)
    } else {
      Left(Seq(s"file '$path' does not exist"))
    }
  }

  private def validateJson(json: String): Either[Seq[String], ValidConfiguration] = {
    try {
      val configurationJson = jsonMarshaller.fromJson(json, classOf[ConfigurationJson])
      configurationJson.validate()
    } catch {
      case ex: Exception =>
        val sampleConfigString = jsonMarshaller.toJson(ConfigurationJson.sample)
        Left(Seq(
          "Unable to read json from 'file name': " + ex.getMessage,
          "A valid configuration might look something like this:",
          sampleConfigString
        ))
    }
  }
}
