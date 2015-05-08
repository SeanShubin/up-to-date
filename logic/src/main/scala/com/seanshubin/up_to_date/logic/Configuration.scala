package com.seanshubin.up_to_date.logic

import java.nio.file.Path

case class Configuration(pomFileName: String,
                         directoriesToSearch: Seq[Path],
                         directoryNamesToSkip: Set[String],
                         mavenRepositories: Seq[String],
                         doNotUpgradeFrom: Set[GroupAndArtifact],
                         doNotUpgradeTo: Set[GroupArtifactVersion],
                         automaticallyUpgrade: Boolean,
                         reportDirectory: Path,
                         cacheDirectory: Path,
                         cacheExpire: String,
                          substitutions:Map[String, String])
