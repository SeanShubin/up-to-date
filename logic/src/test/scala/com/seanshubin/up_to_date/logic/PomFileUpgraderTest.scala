package com.seanshubin.up_to_date.logic

import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class PomFileUpgraderTest extends FunSuite with EasyMockSugar {
  test("automatically update") {
    val upgradesForPom1: Map[GroupAndArtifact, String] = Map(
      GroupAndArtifact("group-1", "artifact-1") -> "upgrade-1",
      GroupAndArtifact("group-2", "artifact-2") -> "upgrade-2"
    )
    val upgradesForPom2: Map[GroupAndArtifact, String] = Map(
      GroupAndArtifact("group-3", "artifact-3") -> "upgrade-3",
      GroupAndArtifact("group-4", "artifact-4") -> "upgrade-4"
    )
    val upgradesByPom: Map[String, Map[GroupAndArtifact, String]] = Map(
      "pom-1" -> upgradesForPom1,
      "pom-2" -> upgradesForPom2
    )
    val fileSystem = mock[FileSystem]
    val pomXmlUpgrader = mock[PomXmlUpgrader]
    val allowAutomaticUpgrades = true
    val pomFileUpgrader = new PomFileUpgraderImpl(fileSystem, pomXmlUpgrader, allowAutomaticUpgrades)
    expecting {
      fileSystem.loadString(Paths.get("pom-1")).andReturn("fake contents 1")
      fileSystem.loadString(Paths.get("pom-2")).andReturn("fake contents 2")
      pomXmlUpgrader.upgrade("fake contents 1", upgradesForPom1).andReturn("new pom 1")
      pomXmlUpgrader.upgrade("fake contents 2", upgradesForPom2).andReturn("new pom 2")
      fileSystem.storeString(Paths.get("pom-1"), "new pom 1")
      fileSystem.storeString(Paths.get("pom-2"), "new pom 2")
    }
    whenExecuting(fileSystem, pomXmlUpgrader) {
      pomFileUpgrader.performAutomaticUpgradesIfApplicable(upgradesByPom)
    }
  }

  test("don't automatically upgrade if flag not set") {
    val upgradesForPom1: Map[GroupAndArtifact, String] = Map(
      GroupAndArtifact("group-1", "artifact-1") -> "upgrade-1",
      GroupAndArtifact("group-2", "artifact-2") -> "upgrade-2"
    )
    val upgradesForPom2: Map[GroupAndArtifact, String] = Map(
      GroupAndArtifact("group-3", "artifact-3") -> "upgrade-3",
      GroupAndArtifact("group-4", "artifact-4") -> "upgrade-4"
    )
    val upgradesByPom: Map[String, Map[GroupAndArtifact, String]] = Map(
      "pom-1" -> upgradesForPom1,
      "pom-2" -> upgradesForPom2
    )
    val fileSystem = mock[FileSystem]
    val pomXmlUpgrader = mock[PomXmlUpgrader]
    val allowAutomaticUpgrades = false
    val pomFileUpgrader = new PomFileUpgraderImpl(fileSystem, pomXmlUpgrader, allowAutomaticUpgrades)
    expecting {
    }
    whenExecuting(fileSystem, pomXmlUpgrader) {
      pomFileUpgrader.performAutomaticUpgradesIfApplicable(upgradesByPom)
    }
  }

  test("preserve line separators") {
    val upgrades: Map[GroupAndArtifact, String] = Map()
    val upgradesByPom: Map[String, Map[GroupAndArtifact, String]] = Map("pom-1" -> upgrades)
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
      pomFileUpgrader.performAutomaticUpgradesIfApplicable(upgradesByPom)
    }
  }
}
