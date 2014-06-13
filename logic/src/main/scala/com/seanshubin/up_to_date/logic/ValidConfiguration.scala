package com.seanshubin.up_to_date.logic

case class ValidConfiguration(pomDirectories: Seq[String],
                              mavenRepositories: Seq[String],
                              automaticallyUpdate: Seq[Seq[String]],
                              ignore: Seq[Seq[String]],
                              reportDirectory: String,
                              cacheDirectory: String,
                              cacheExpireMilliseconds: Long)

