package com.seanshubin.up_to_date.logic

class ConfigurationValidatorImpl(fileSystem: FileSystem,
                                 jsonMarshaller: JsonMarshaller) extends ConfigurationValidator {
  override def validate(commandLineArguments: Seq[String]): Either[Seq[String], ValidConfiguration] = {
    if (commandLineArguments.size < 1) {
      Left(Seq("at least one command line argument required"))
    } else if (commandLineArguments.size > 1) {
      Left(Seq("no more than one command line argument allowed"))
    } else {
      validateFile(commandLineArguments(0))
    }
  }

  private def validateFile(fileName: String): Either[Seq[String], ValidConfiguration] = {
    if (fileSystem.fileExists(fileName)) {
      val json = fileSystem.loadFileIntoString(fileName)
      validateJson(json)
    } else {
      Left(Seq(s"file '$fileName' does not exist"))
    }
  }

  private def validateJson(json: String): Either[Seq[String], ValidConfiguration] = {
    try {
      val configurationJson = jsonMarshaller.fromJson(json, classOf[ConfigurationJson])
      configurationJson.validate()
    } catch {
      case ex: RuntimeException => Left(Seq("Unable to read json from 'file name': " + ex.getMessage))
    }
  }
}
