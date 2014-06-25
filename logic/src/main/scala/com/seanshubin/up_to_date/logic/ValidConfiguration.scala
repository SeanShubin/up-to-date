package com.seanshubin.up_to_date.logic

case class ValidConfiguration(pomFileName: String,
                              directoriesToSearch: Seq[String],
                              directoryNamesToSkip: Seq[String],
                              mavenRepositories: Seq[String],
                              dependenciesToAutomaticallyUpdate: Seq[Seq[String]],
                              ignore: Seq[Seq[String]],
                              reportDirectory: String,
                              cacheDirectory: String,
                              cacheExpireMilliseconds: Long)
