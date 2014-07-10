package com.seanshubin.up_to_date.logic

import scala.annotation.tailrec

case class Version(words: List[String]) extends Ordered[Version] {
  def this(version: String) = this(Version.breakIntoWords(version))

  def isRelease: Boolean = words.forall(Version.isNumberOrReleaseWord)

  def shouldUpgradeTo(that: Version): Boolean = {
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

  def shouldUpgradeTo(that: String): Boolean = shouldUpgradeTo(Version(that))

  override def compare(that: Version): Int = {
    val compareResult = Version.compareWordLists(this.words, that.words)
    compareResult
  }

  def dropReleaseCandidateParts: Version = {
    Version(words.takeWhile(Version.notReleaseCandidate))
  }

  def selectUpgrade(versions: Set[Version]): Option[Version] = {
    def shouldUpgrade(that: Version) = shouldUpgradeTo(that)
    versions.filter(shouldUpgrade).toSeq.sorted.headOption
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
  private val releaseCandidateWords = Set("rc")

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
}
