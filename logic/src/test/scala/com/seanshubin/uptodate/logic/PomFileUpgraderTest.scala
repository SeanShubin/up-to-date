package com.seanshubin.uptodate.logic

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.easymock.EasyMockSugar

class PomFileUpgraderTest extends FunSuite with EasyMockSugar {
  test("automatically update") {
    val upgradesForPom1 = Seq(
      Upgrade("pom-1", "group-1", "artifact-1", "verison-1", "upgrade-1"),
      Upgrade("pom-1", "group-2", "artifact-2", "version-2", "upgrade-2")
    )
    val upgradesForPom2 = Seq(
      Upgrade("pom-2", "group-3", "artifact-3", "version-3", "upgrade-3"),
      Upgrade("pom-2", "group-4", "artifact-4", "version-4", "upgrade-4")
    )
    val upgrades = upgradesForPom1 ++ upgradesForPom2
    val fileSystem = mock[FileSystem]
    val pomXmlUpgrader = mock[PomXmlUpgrader]
    val allowAutomaticUpgrades = true
    val pomFileUpgrader = new PomFileUpgraderImpl(fileSystem, pomXmlUpgrader, allowAutomaticUpgrades)
    expecting {
      fileSystem.loadString(Paths.get("pom-1")).andReturn("contents 1")
      fileSystem.loadString(Paths.get("pom-2")).andReturn("contents 2")
      pomXmlUpgrader.upgrade("contents 1", upgradesForPom1).andReturn("new pom 1")
      pomXmlUpgrader.upgrade("contents 2", upgradesForPom2).andReturn("new pom 2")
      fileSystem.storeString(Paths.get("pom-1"), "new pom 1")
      fileSystem.storeString(Paths.get("pom-2"), "new pom 2")
    }
    whenExecuting(fileSystem, pomXmlUpgrader) {
      pomFileUpgrader.performAutomaticUpgradesIfApplicable(upgrades)
    }
  }

  test("don't automatically upgrade if flag not set") {
    val upgradesForPom1 = Seq(
      Upgrade("pom-1", "group-1", "artifact-1", "verison-1", "upgrade-1"),
      Upgrade("pom-1", "group-2", "artifact-2", "version-2", "upgrade-2")
    )
    val upgradesForPom2 = Seq(
      Upgrade("pom-2", "group-3", "artifact-3", "version-3", "upgrade-3"),
      Upgrade("pom-2", "group-4", "artifact-4", "version-4", "upgrade-4")
    )
    val upgrades = upgradesForPom1 ++ upgradesForPom2
    val fileSystem = mock[FileSystem]
    val pomXmlUpgrader = mock[PomXmlUpgrader]
    val allowAutomaticUpgrades = false
    val pomFileUpgrader = new PomFileUpgraderImpl(fileSystem, pomXmlUpgrader, allowAutomaticUpgrades)
    expecting {
    }
    whenExecuting(fileSystem, pomXmlUpgrader) {
      pomFileUpgrader.performAutomaticUpgradesIfApplicable(upgrades)
    }
  }

  test("preserve line separators") {
    val upgradesForPom1 = Seq(
      Upgrade("pom-1", "group-1", "artifact-1", "verison-1", "upgrade-1"),
      Upgrade("pom-1", "group-2", "artifact-2", "version-2", "upgrade-2")
    )
    val upgrades = upgradesForPom1
    val fileSystem = mock[FileSystem]
    val pomXmlUpgrader = mock[PomXmlUpgrader]
    val allowAutomaticUpgrades = true
    val pomFileUpgrader = new PomFileUpgraderImpl(fileSystem, pomXmlUpgrader, allowAutomaticUpgrades)
    val originalFileContents = "aaa\nbbb"
    val processedFileContents = "aaa\r\nccc"
    val newFileContents = "aaa\nccc"
    expecting {
      fileSystem.loadString(Paths.get("pom-1")).andReturn(originalFileContents)
      pomXmlUpgrader.upgrade(originalFileContents, upgrades).andReturn(processedFileContents)
      fileSystem.storeString(Paths.get("pom-1"), newFileContents)
    }
    whenExecuting(fileSystem, pomXmlUpgrader) {
      pomFileUpgrader.performAutomaticUpgradesIfApplicable(upgrades)
    }
  }
}
