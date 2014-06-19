package com.seanshubin.up_to_date.logic

import org.scalatest.{ShouldMatchers, FunSuite}

class DependencyTest extends FunSuite with ShouldMatchers {
  test("create") {
    val actual = Dependency.create("org.codehaus.groovy", "groovy-eclipse-compiler", "2.7.0-01")
    val expected = Dependency("org.codehaus.groovy.groovy-eclipse-compiler", "org.codehaus.groovy", "groovy-eclipse-compiler", "2.7.0-01")
    assert(expected === actual)
  }

  test("create when version in artifact id") {
    val actual = Dependency.create("com.fasterxml.jackson.module", "jackson-module-scala_2.11", "2.4.0-rc2")
    val expected = Dependency("com.fasterxml.jackson.module.jackson-module-scala", "com.fasterxml.jackson.module", "jackson-module-scala_2.11", "2.4.0-rc2")
    assert(expected === actual)
  }

  test("simple version comparison") {
    val later = Dependency.create("joda-time", "joda-time", "2.3")
    val earlier = Dependency.create("joda-time", "joda-time", "1.4")
    earlier should be < later
  }

  test("version words") {
    assert(Dependency.versionWords("2.11.1") === Seq("2", "11", "1"))
    assert(Dependency.versionWords("2.3") === Seq("2", "3"))
    assert(Dependency.versionWords("20050911") === Seq("20050911"))
    assert(Dependency.versionWords("1.8.0.7") === Seq("1", "8", "0", "7"))
    assert(Dependency.versionWords("2.7.0-01") === Seq("2", "7", "0", "01"))
    assert(Dependency.versionWords("version-unspecified") === Seq("version", "unspecified"))
    assert(Dependency.versionWords("1.6.5.aa.3") === Seq("1", "6", "5", "aa", "3"))
    assert(Dependency.versionWords("3.3.1.GA") === Seq("3", "3", "1", "GA"))
    assert(Dependency.versionWords("4.1.0.Final") === Seq("4", "1", "0", "Final"))
    assert(Dependency.versionWords("0.7-groovy-2.0") === Seq("0", "7", "groovy", "2", "0"))
    assert(Dependency.versionWords("1.3rc1") === Seq("1", "3", "rc", "1"))
    assert(Dependency.versionWords("1.2alpha2") === Seq("1", "2", "alpha", "2"))
    assert(Dependency.versionWords("1.2_Java1.3") === Seq("1", "2", "Java", "1", "3"))
    assert(Dependency.versionWords("0.3.1-SNAPSHOT") === Seq("0", "3", "1", "SNAPSHOT"))
    assert(Dependency.versionWords("2.0-rc1") === Seq("2", "0", "rc", "1"))
    assert(Dependency.versionWords("1.0-M1") === Seq("1", "0", "M", "1"))
    assert(Dependency.versionWords("1.7R2") === Seq("1", "7", "R", "2"))
    assert(Dependency.versionWords("7.6.10.v20130312") === Seq("7", "6", "10", "v", "20130312"))
    assert(Dependency.versionWords("1.0.0.M2") === Seq("1", "0", "0", "M", "2"))
    assert(Dependency.versionWords("0.0.3.aa") === Seq("0", "0", "3", "aa"))
    assert(Dependency.versionWords("0.3m") === Seq("0", "3", "m"))
    assert(Dependency.versionWords("2.4.0-rc2") === Seq("2", "4", "0", "rc", "2"))
    assert(Dependency.versionWords("0.5.6-patch1") === Seq("0", "5", "6", "patch", "1"))
  }

  test("is production version") {
    assert(Dependency.isRelease("2.11.1") === true)
    assert(Dependency.isRelease("2.3") === true)
    assert(Dependency.isRelease("20050911") === true)
    assert(Dependency.isRelease("1.8.0.7") === true)
    assert(Dependency.isRelease("2.7.0-01") === true)
    assert(Dependency.isRelease("0.5.6-patch1") === true)
    assert(Dependency.isRelease("version-unspecified") === false)
    assert(Dependency.isRelease("1.6.5.aa.3") === false)
    assert(Dependency.isRelease("3.3.1.GA") === true)
    assert(Dependency.isRelease("4.1.0.Final") === true)
    assert(Dependency.isRelease("1.3rc1") === false)
    assert(Dependency.isRelease("1.2alpha2") === false)
    assert(Dependency.isRelease("1.2_Java1.3") === true)
    assert(Dependency.isRelease("0.3.1-SNAPSHOT") === false)
    assert(Dependency.isRelease("2.0-rc1") === false)
    assert(Dependency.isRelease("1.0-M1") === false)
    assert(Dependency.isRelease("0.7-groovy-2.0") === true)
    assert(Dependency.isRelease("1.7R2") === true)
    assert(Dependency.isRelease("7.6.10.v20130312") === true)
    assert(Dependency.isRelease("1.0.0.M2") === false)
    assert(Dependency.isRelease("0.0.3.aa") === false)
    assert(Dependency.isRelease("0.3m") === false)
    assert(Dependency.isRelease("2.4.0-rc2") === false)
  }

  test("words are less than numbers") {
    assert(Dependency.compare("1.a.5", "1.a.5") === 0)
    assert(Dependency.compare("1.a.5", "1.0.5") === -1)
    assert(Dependency.compare("1.a.5", "1.5") === -1)
    assert(Dependency.compare("1.0.5", "1.a.5") === 1)
    assert(Dependency.compare("1.0.5", "1.0.5") === 0)
    assert(Dependency.compare("1.0.5", "1.5") === -1)
    assert(Dependency.compare("1.5", "1.a.5") === 1)
    assert(Dependency.compare("1.5", "1.0.5") === 1)
    assert(Dependency.compare("1.5", "1.5") === 0)
  }

  test("words equal each other") {
    assert(Dependency.compare("1.a.5", "1.b.5") === 0)
  }

  test("missing less than present") {
    assert(Dependency.compare("1.2", "1.2.0") === -1)
    assert(Dependency.compare("1.2", "1.2.a") === -1)
  }

  test("upgrade path") {
    assert(Dependency.shouldUpgrade("1.1", "1.1") === false)
    assert(Dependency.shouldUpgrade("1.1", "1.2") === true)
    assert(Dependency.shouldUpgrade("1.1", "1.2-rc3") === false)
    assert(Dependency.shouldUpgrade("1.1", "1.2-rc4") === false)
    assert(Dependency.shouldUpgrade("1.1", "1.3-rc1") === false)

    assert(Dependency.shouldUpgrade("1.2", "1.1") === false)
    assert(Dependency.shouldUpgrade("1.2", "1.2") === false)
    assert(Dependency.shouldUpgrade("1.2", "1.2-rc3") === false)
    assert(Dependency.shouldUpgrade("1.2", "1.2-rc4") === false)
    assert(Dependency.shouldUpgrade("1.2", "1.3-rc1") === false)

    assert(Dependency.shouldUpgrade("1.2-rc3", "1.1") === false)
    //todo: A bit tricky to figure out when to upgrade a release candidate to its corresponding version
    //assert(Dependency.shouldUpgrade("1.2-rc3", "1.2") === true)
    assert(Dependency.shouldUpgrade("1.2-rc3", "1.2-rc3") === false)
    assert(Dependency.shouldUpgrade("1.2-rc3", "1.2-rc4") === true)
    assert(Dependency.shouldUpgrade("1.2-rc3", "1.3-rc1") === true)

    assert(Dependency.shouldUpgrade("1.2-rc4", "1.1") === false)
    //todo: A bit tricky to figure out when to upgrade a release candidate to its corresponding version
    //assert(Dependency.shouldUpgrade("1.2-rc4", "1.2") === true)
    assert(Dependency.shouldUpgrade("1.2-rc4", "1.2-rc3") === false)
    assert(Dependency.shouldUpgrade("1.2-rc4", "1.2-rc4") === false)
    assert(Dependency.shouldUpgrade("1.2-rc4", "1.3-rc1") === true)

    assert(Dependency.shouldUpgrade("1.3-rc1", "1.1") === false)
    assert(Dependency.shouldUpgrade("1.3-rc1", "1.2") === false)
    assert(Dependency.shouldUpgrade("1.3-rc1", "1.2-rc3") === false)
    assert(Dependency.shouldUpgrade("1.3-rc1", "1.2-rc4") === false)
    assert(Dependency.shouldUpgrade("1.3-rc1", "1.3-rc1") === false)
  }
}
