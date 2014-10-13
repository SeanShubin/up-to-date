package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

class PomFileUpgraderImpl(fileSystem: FileSystem,
                          pomXmlUpgrader: PomXmlUpgrader,
                          allowAutomaticUpgrades: Boolean) extends PomFileUpgrader {
  override def performAutomaticUpgradesIfApplicable(upgrades: Seq[Upgrade]) {
    if (allowAutomaticUpgrades) {
      val upgradesByPom = Upgrade.groupByLocation(upgrades)
      performAutomaticUpgrades(upgradesByPom)
    }
  }

  private def performAutomaticUpgrades(upgradesByPom: Map[String, Seq[Upgrade]]) {
    for {
      (pom, upgrades) <- upgradesByPom
    } {
      val path = Paths.get(pom)
      val oldPomText = fileSystem.loadString(path)
      val oldNewlineSeparator = StringUtil.getNewlineSeparator(pom, oldPomText, "\n")
      val newPomTextWithDefaultNewlineSeparator = pomXmlUpgrader.upgrade(oldPomText, upgrades)
      val newPomText = StringUtil.replaceNewlineSeparator(newPomTextWithDefaultNewlineSeparator, oldNewlineSeparator)
      fileSystem.storeString(path, newPomText)
    }
  }
}
