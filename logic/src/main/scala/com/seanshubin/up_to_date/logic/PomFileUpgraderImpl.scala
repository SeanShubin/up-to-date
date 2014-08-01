package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

class PomFileUpgraderImpl(fileSystem: FileSystem,
                          pomXmlUpgrader: PomXmlUpgrader,
                          allowAutomaticUpgrades: Boolean) extends PomFileUpgrader {
  override def performAutomaticUpgradesIfApplicable(upgradesByPom: Map[String, Map[GroupAndArtifact, String]]) {
    if (allowAutomaticUpgrades) {
      performAutomaticUpgrades(upgradesByPom)
    }
  }

  private def performAutomaticUpgrades(upgradesByPom: Map[String, Map[GroupAndArtifact, String]]) {
    for {
      (pom, upgrades) <- upgradesByPom
    } {
      val path = Paths.get(pom)
      val oldPomText = fileSystem.loadString(path)
      val newPomText = pomXmlUpgrader.upgrade(oldPomText, upgrades)
      fileSystem.storeString(path, newPomText)
    }
  }
}
