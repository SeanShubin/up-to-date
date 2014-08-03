package com.seanshubin.up_to_date.logic

import java.nio.charset.Charset

import org.scalatest.FunSuite

class PomXmlUpgraderTest extends FunSuite {
  test("modify xml") {
    val inputXml =
      """<xml>
        |  <dependency>
        |    <groupId>group-1</groupId>
        |    <artifactId>artifact-1</artifactId>
        |    <version>version-1</version>
        |  </dependency>
        |  <dependency>
        |    <groupId>group-2</groupId>
        |    <artifactId>artifact-2</artifactId>
        |    <version>version-2</version>
        |  </dependency>
        |</xml>
      """.stripMargin

    val upgrades = Seq(
      Upgrade("pom", "group-1", "artifact-1", "version-1", "upgrade-1"),
      Upgrade("pom", "group-2", "artifact-2", "version-2", "upgrade-2")
    )
    val charsetName = "utf-8"
    val charset = Charset.forName(charsetName)
    val doNotUpgradeFrom = Set[GroupAndArtifact]()
    val doNotUpgradeTo = Set[GroupArtifactVersion]()
    val pomXmlUpgrader = new PomXmlUpgraderImpl(charset, doNotUpgradeFrom, doNotUpgradeTo)
    val actual = pomXmlUpgrader.upgrade(inputXml, upgrades)
    val expected =
      """<?xml version="1.0" encoding="UTF-8" standalone="no"?><xml>
        |  <dependency>
        |    <groupId>group-1</groupId>
        |    <artifactId>artifact-1</artifactId>
        |    <version>upgrade-1</version>
        |  </dependency>
        |  <dependency>
        |    <groupId>group-2</groupId>
        |    <artifactId>artifact-2</artifactId>
        |    <version>upgrade-2</version>
        |  </dependency>
        |</xml>""".stripMargin
    assert(actual === expected)
  }

  test("ignore do not upgrade to") {
    val inputXml =
      """<?xml version="1.0" encoding="UTF-8" standalone="no"?><xml>
        |  <dependency>
        |    <groupId>group-1</groupId>
        |    <artifactId>artifact-1</artifactId>
        |    <version>version-1</version>
        |  </dependency>
        |</xml>""".stripMargin
    val upgrades = Seq(
      Upgrade("pom", "group-1", "artifact-1", "version-1", "upgrade-1")
    )
    val doNotUpgradeFrom = Set[GroupAndArtifact]()
    val doNotUpgradeTo = Set(GroupArtifactVersion("group-1", "artifact-1", "upgrade-1"))
    val charsetName = "utf-8"
    val charset = Charset.forName(charsetName)
    val pomXmlUpgrader = new PomXmlUpgraderImpl(charset, doNotUpgradeFrom, doNotUpgradeTo)
    val actual = pomXmlUpgrader.upgrade(inputXml, upgrades)
    assert(actual === inputXml)
  }

  test("ignore do not upgrade from") {
    val inputXml =
      """<?xml version="1.0" encoding="UTF-8" standalone="no"?><xml>
        |  <dependency>
        |    <groupId>group-1</groupId>
        |    <artifactId>artifact-1</artifactId>
        |    <version>version-1</version>
        |  </dependency>
        |</xml>""".stripMargin
    val upgrades = Seq(
      Upgrade("pom", "group-1", "artifact-1", "version-1", "upgrade-1")
    )
    val charsetName = "utf-8"
    val charset = Charset.forName(charsetName)
    val doNotUpgradeFrom = Set(GroupAndArtifact("group-1", "artifact-1"))
    val doNotUpgradeTo = Set[GroupArtifactVersion]()
    val pomXmlUpgrader = new PomXmlUpgraderImpl(charset, doNotUpgradeFrom, doNotUpgradeTo)
    val actual = pomXmlUpgrader.upgrade(inputXml, upgrades)
    assert(actual === inputXml)
  }
}
