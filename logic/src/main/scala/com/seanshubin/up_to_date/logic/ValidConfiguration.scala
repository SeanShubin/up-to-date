package com.seanshubin.up_to_date.logic

import java.nio.file.Path

case class ValidConfiguration(pomFileName: String,
                              directoriesToSearch: Seq[Path],
                              directoryNamesToSkip: Seq[String],
                              mavenRepositories: Seq[String],
                              doNotUpgradeFrom: Set[GroupAndArtifact],
                              doNotUpgradeTo: Set[GroupArtifactVersion],
                              automaticallyUpgrade: Boolean,
                              reportDirectory: Path,
                              cacheDirectory: Path,
                              cacheExpireMilliseconds: Long)
