package com.seanshubin.uptodate.logic

trait ConfigurationValidator {
  def validate(commandLineArguments: Seq[String]): Either[Seq[String], Configuration]
}
