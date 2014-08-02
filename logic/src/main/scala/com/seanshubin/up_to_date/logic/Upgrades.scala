package com.seanshubin.up_to_date.logic

case class Upgrades(upgradesByPom: Map[String, Map[GroupAndArtifact, String]],
                    skippedUpgradesByPom: Map[String, Map[GroupArtifactVersion, String]],
                    doNotUpgradeFrom: Set[GroupAndArtifact],
                    doNotUpgradeTo: Set[GroupArtifactVersion],
                    ignoreToPreserveStatusQuo:Seq[Seq[String]])
