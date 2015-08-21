package com.seanshubin.up_to_date.logic

import org.scalatest.{FunSuite, ShouldMatchers}

class VersionTest extends FunSuite with ShouldMatchers {
  test("version words") {
    assert(Version("2.11.1") === Version("2.11.1", List("2", "11", "1")))
    assert(Version("2.3") === Version("2.3", List("2", "3")))
    assert(Version("20050911") === Version("20050911", List("20050911")))
    assert(Version("1.8.0.7") === Version("1.8.0.7", List("1", "8", "0", "7")))
    assert(Version("2.7.0-01") === Version("2.7.0-01", List("2", "7", "0", "01")))
    assert(Version("version-unspecified") === Version("version-unspecified", List("version", "unspecified")))
    assert(Version("1.6.5.aa.3") === Version("1.6.5.aa.3", List("1", "6", "5", "aa", "3")))
    assert(Version("3.3.1.GA") === Version("3.3.1.GA", List("3", "3", "1", "GA")))
    assert(Version("4.1.0.Final") === Version("4.1.0.Final", List("4", "1", "0", "Final")))
    assert(Version("0.7-groovy-2.0") === Version("0.7-groovy-2.0", List("0", "7", "groovy", "2", "0")))
    assert(Version("1.3rc1") === Version("1.3rc1", List("1", "3", "rc", "1")))
    assert(Version("1.2alpha2") === Version("1.2alpha2", List("1", "2", "alpha", "2")))
    assert(Version("1.2_Java1.3") === Version("1.2_Java1.3", List("1", "2", "Java", "1", "3")))
    assert(Version("0.3.1-SNAPSHOT") === Version("0.3.1-SNAPSHOT", List("0", "3", "1", "SNAPSHOT")))
    assert(Version("2.0-rc1") === Version("2.0-rc1", List("2", "0", "rc", "1")))
    assert(Version("1.0-M1") === Version("1.0-M1", List("1", "0", "M", "1")))
    assert(Version("1.7R2") === Version("1.7R2", List("1", "7", "R", "2")))
    assert(Version("7.6.10.v20130312") === Version("7.6.10.v20130312", List("7", "6", "10", "v", "20130312")))
    assert(Version("1.0.0.M2") === Version("1.0.0.M2", List("1", "0", "0", "M", "2")))
    assert(Version("0.0.3.aa") === Version("0.0.3.aa", List("0", "0", "3", "aa")))
    assert(Version("0.3m") === Version("0.3m", List("0", "3", "m")))
    assert(Version("2.4.0-rc2") === Version("2.4.0-rc2", List("2", "4", "0", "rc", "2")))
    assert(Version("0.5.6-patch1") === Version("0.5.6-patch1", List("0", "5", "6", "patch", "1")))
  }

  test("is production version") {
    assert(Version("2.11.1").isRelease === true)
    assert(Version("2.3").isRelease === true)
    assert(Version("20050911").isRelease === true)
    assert(Version("1.8.0.7").isRelease === true)
    assert(Version("2.7.0-01").isRelease === true)
    assert(Version("0.5.6-patch1").isRelease === true)
    assert(Version("version-unspecified").isRelease === false)
    assert(Version("1.6.5.aa.3").isRelease === false)
    assert(Version("3.3.1.GA").isRelease === true)
    assert(Version("4.1.0.Final").isRelease === true)
    assert(Version("1.3rc1").isRelease === false)
    assert(Version("1.2alpha2").isRelease === false)
    assert(Version("1.2_Java1.3").isRelease === true)
    assert(Version("0.3.1-SNAPSHOT").isRelease === false)
    assert(Version("2.0-rc1").isRelease === false)
    assert(Version("1.0-M1").isRelease === false)
    assert(Version("0.7-groovy-2.0").isRelease === true)
    assert(Version("1.7R2").isRelease === true)
    assert(Version("7.6.10.v20130312").isRelease === true)
    assert(Version("1.0.0.M2").isRelease === false)
    assert(Version("0.0.3.aa").isRelease === false)
    assert(Version("0.3m").isRelease === false)
    assert(Version("2.4.0-rc2").isRelease === false)
  }

  test("words are less than numbers") {
    assert(Version("1.a.5") === Version("1.a.5"))
    assert(Version("1.a.5") < Version("1.0.5"))
    assert(Version("1.a.5") < Version("1.5"))
    assert(Version("1.0.5") > Version("1.a.5"))
    assert(Version("1.0.5") === Version("1.0.5"))
    assert(Version("1.0.5") < Version("1.5"))
    assert(Version("1.5") > Version("1.a.5"))
    assert(Version("1.5") > Version("1.0.5"))
    assert(Version("1.5") === Version("1.5"))
  }

  test("comparator does not apply ordering to words") {
    assert(Version("1.a.5") < Version("1.b.5") === false)
    assert(Version("1.a.5") > Version("1.b.5") === false)
    assert(Version("1.b.5") < Version("1.a.5") === false)
    assert(Version("1.b.5") > Version("1.a.5") === false)
  }

  test("missing less than present") {
    assert(Version("1.2") < Version("1.2.0"))
    assert(Version("1.2") < Version("1.2.a"))
  }

  test("upgrade path") {
    assert(Version("1.1").shouldUpgradeTo("1.1") === false)
    assert(Version("1.1").shouldUpgradeTo("1.2") === true)
    assert(Version("1.1").shouldUpgradeTo("1.2-rc3") === false)
    assert(Version("1.1").shouldUpgradeTo("1.2-rc4") === false)
    assert(Version("1.1").shouldUpgradeTo("1.3-rc1") === false)

    assert(Version("1.2").shouldUpgradeTo("1.1") === false)
    assert(Version("1.2").shouldUpgradeTo("1.2") === false)
    assert(Version("1.2").shouldUpgradeTo("1.2-rc3") === false)
    assert(Version("1.2").shouldUpgradeTo("1.2-rc4") === false)
    assert(Version("1.2").shouldUpgradeTo("1.3-rc1") === false)

    assert(Version("1.2-rc3").shouldUpgradeTo("1.1") === false)
    assert(Version("1.2-rc3").shouldUpgradeTo("1.2") === true)
    assert(Version("1.2-rc3").shouldUpgradeTo("1.2-rc3") === false)
    assert(Version("1.2-rc3").shouldUpgradeTo("1.2-rc4") === true)
    assert(Version("1.2-rc3").shouldUpgradeTo("1.3-rc1") === false)

    assert(Version("1.2-rc4").shouldUpgradeTo("1.1") === false)
    assert(Version("1.2-rc4").shouldUpgradeTo("1.2") === true)
    assert(Version("1.2-rc4").shouldUpgradeTo("1.2-rc3") === false)
    assert(Version("1.2-rc4").shouldUpgradeTo("1.2-rc4") === false)
    assert(Version("1.2-rc4").shouldUpgradeTo("1.3-rc1") === false)

    assert(Version("1.3-rc1").shouldUpgradeTo("1.1") === false)
    assert(Version("1.3-rc1").shouldUpgradeTo("1.2") === false)
    assert(Version("1.3-rc1").shouldUpgradeTo("1.2-rc3") === false)
    assert(Version("1.3-rc1").shouldUpgradeTo("1.2-rc4") === false)
    assert(Version("1.3-rc1").shouldUpgradeTo("1.3-rc1") === false)

    assert(Version("1.0-SNAPSHOT").shouldUpgradeTo("1.0") === true)
    assert(Version("1.1-SNAPSHOT").shouldUpgradeTo("1.0") === false)
    assert(Version("1.0.1-SNAPSHOT").shouldUpgradeTo("1.0") === false)
    assert(Version("${project.version}").shouldUpgradeTo("1.0") === false)
  }

  test("select upgrade") {
    val allVersionStrings = Set("1.1", "1.2", "1.2-rc3", "1.2-rc4", "1.3-rc1", "1.3-rc2", "1.4-rc1")
    val allVersions = allVersionStrings.map(Version.apply)
    checkUpgradePath(allVersions, "1.1", Some("1.2"))
    checkUpgradePath(allVersions, "1.2", None)
    checkUpgradePath(allVersions, "1.2-rc3", Some("1.2"))
    checkUpgradePath(allVersions, "1.2-rc4", Some("1.2"))
    checkUpgradePath(allVersions, "1.3-rc1", Some("1.3-rc2"))
    checkUpgradePath(allVersions, "1.3-rc2", None)
  }

  test("select upgrade 2") {
    val allVersionStrings = Set("2.11.1", "2.10", "2.11.0")
    val allVersions = allVersionStrings.map(Version.apply)
    checkUpgradePath(allVersions, "2.10", Some("2.11.1"))
  }

  test("best available") {
    val allVersions = Set("1.1", "1.2", "1.2-rc3", "1.2-rc4", "1.3-rc1", "1.3-rc2", "1.4-rc1").map(Version.apply)
    assert(Version.bestAvailableVersionFrom(allVersions).originalString === "1.2")
  }

  test("best available when no release versions") {
    val allVersions = Set("1.2-rc3", "1.2-rc4", "1.3-rc1", "1.3-rc2", "1.4-rc1").map(Version.apply)
    assert(Version.bestAvailableVersionFrom(allVersions).originalString === "1.4-rc1")
  }

  def checkUpgradePath(allVersions: Set[Version], from: String, to: Option[String]) {
    assert(Version(from).selectUpgrade(allVersions) === to.map(Version.apply))
  }
}
