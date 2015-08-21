package com.seanshubin.up_to_date.logic

import scala.annotation.tailrec

case class Version(originalString: String, words: List[String]) extends Ordered[Version] {
  def this(version: String) = this(version, Version.breakIntoWords(version))

  def isRelease: Boolean = words.forall(Version.isNumberOrReleaseWord)

  def isVariable:Boolean = originalString.contains("$")

  def shouldUpgradeTo(that: Version): Boolean = {
    if (this.isVariable || that.isVariable) {
      false
    } else {
      if (this.isRelease) {
        if (that.isRelease) {
          if (this < that) {
            true
          } else {
            false
          }
        } else {
          false
        }
      } else {
        if (that.isRelease) {
          if (this.dropReleaseCandidateParts > that) {
            false
          } else {
            true
          }
        } else {
          if (this < that) {
            if (this.dropReleaseCandidateParts < that.dropReleaseCandidateParts) {
              false
            } else {
              true
            }
          } else {
            false
          }
        }
      }
    }
  }

  def shouldUpgradeTo(that: String): Boolean = shouldUpgradeTo(Version(that))

  override def compare(that: Version): Int = {
    val compareResult = Version.compareWordLists(this.words, that.words)
    compareResult
  }

  def dropReleaseCandidateParts: Version = {
    copy(words = words.takeWhile(Version.notReleaseCandidate))
  }

  def selectUpgrade(versions: Set[Version]): Option[Version] = {
    def shouldUpgrade(that: Version) = shouldUpgradeTo(that)
    val releases = versions.filter(shouldUpgrade).filter(_.isRelease)
    val potentialUpgrades = if (releases.isEmpty) {
      versions.filter(shouldUpgrade)
    } else {
      releases
    }
    potentialUpgrades.toSeq.sorted.reverse.headOption
  }
}

object Version {
  def apply(version: String): Version = new Version(version)

  private val WholeNumberPattern = """[0-9]+"""
  private val WordPattern = """[a-zA-Z]+"""
  private val OrPattern = "|"
  private val NumberOrWordPattern = capture(WholeNumberPattern) + OrPattern + capture(WordPattern)
  private val NumberOrWordRegex = NumberOrWordPattern.r

  private def capture(pattern: String) = "(" + pattern + ")"

  private val releaseWords = Set("ga", "final", "patch", "java", "groovy", "r", "v")
  private val releaseCandidateWords = Set("rc", "SNAPSHOT")

  def notReleaseCandidate(word: String): Boolean = !releaseCandidateWords.contains(word)

  @tailrec
  def compareWordLists(leftVersionWords: List[String], rightVersionWords: List[String]): Int = {
    (leftVersionWords, rightVersionWords) match {
      case (Nil, Nil) => 0
      case (Nil, _) => -1
      case (_, Nil) => 1
      case (left, right) =>
        val headCompare = compareWord(left.head, right.head)
        if (headCompare == 0) compareWordLists(left.tail, right.tail)
        else headCompare
    }
  }

  def breakIntoWords(version: String): List[String] = {
    val words = Version.NumberOrWordRegex.findAllIn(version).toList
    words
  }

  def compareWord(leftVersionWord: String, rightVersionWord: String): Int = {
    val leftNumber = toNumber(leftVersionWord)
    val rightNumber = toNumber(rightVersionWord)
    val compareResult = leftNumber.compareTo(rightNumber)
    compareResult
  }

  def toNumber(versionWord: String): Long = {
    try {
      versionWord.toLong
    } catch {
      case ex: NumberFormatException => -1
    }
  }

  def isNumberOrReleaseWord(s: String) = {
    val isReleaseWord = releaseWords.contains(s.toLowerCase)
    val isNumber = s.matches(WholeNumberPattern)
    isReleaseWord || isNumber
  }

  def bestAvailableVersionFrom(versions: Set[Version]): Version = {
    val releaseVersions = versions.filter(_.isRelease)
    val availableVersions = if (releaseVersions.isEmpty) {
      versions
    } else {
      releaseVersions
    }
    availableVersions.toSeq.sorted.reverse.head
  }

  def selectUpgrade(currentVersionString: String, versionStrings: Seq[String]): Option[String] = {
    val version = Version(currentVersionString)
    val versions = versionStrings.toSet.map(Version.apply)
    val upgrade = version.selectUpgrade(versions)
    upgrade.map(_.originalString)
  }

  def stringDescending(left: String, right: String): Boolean = {
    Version(left).compareTo(Version(right)) > 0
  }
}
