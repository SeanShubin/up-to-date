package com.seanshubin.up_to_date.logic

import java.nio.file.Path

case class ValidConfiguration(pomFileName: String,
                              directoriesToSearch: Seq[Path],
                              directoryNamesToSkip: Seq[String],
                              mavenRepositories: Seq[String],
                              dependenciesToAutomaticallyUpdate: Seq[Seq[String]],
                              ignore: Seq[Seq[String]],
                              reportDirectory: Path,
                              cacheDirectory: Path,
                              cacheExpireMilliseconds: Long)
