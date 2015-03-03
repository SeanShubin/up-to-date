package com.seanshubin.up_to_date.logic

trait ConfigurationValidator {
  def validate(commandLineArguments: Seq[String]): Either[Seq[String], Configuration]
}
