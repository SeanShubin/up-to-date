package com.seanshubin.up_to_date.logic

import scala.annotation.tailrec

case class Dependency(id: String, group: String, artifact: String, version: String) extends Ordered[Dependency] {
  override def compare(that: Dependency): Int = this.version.compare(that.version)
}

object Dependency {
  private val VersionInArtifactId = """(.*)_(.*)""".r
  private val WholeNumberPattern = """[0-9]+"""
  private val WordPattern = """[a-zA-Z]+"""
  private val OrPattern = "|"
  private val NumberOrWordPattern = capture(WholeNumberPattern) + OrPattern + capture(WordPattern)
  private val NumberOrWordRegex = NumberOrWordPattern.r

  private def capture(pattern: String) = "(" + pattern + ")"

  private val releaseWords = Set("ga", "final", "patch", "java", "groovy", "r", "v")

  def create(group: String, artifact: String, version: String) =
    artifact match {
      case VersionInArtifactId(artifactWord, versionWord) =>
        Dependency(group + "." + artifactWord, group: String, artifact: String, version: String)
      case _ =>
        Dependency(group + "." + artifact, group: String, artifact: String, version: String)
    }

  def isRelease(version: String): Boolean = {
    versionWords(version).forall(isNumberOrReleaseWord)
  }

  def versionWords(version: String): Seq[String] = {
    NumberOrWordRegex.findAllIn(version).toSeq
  }

  def compare(leftVersion: String, rightVersion: String): Int = {
    val leftVersionWords = versionWords(leftVersion)
    val rightVersionWords = versionWords(rightVersion)
    val compareResult = compareWordLists(leftVersionWords.toList, rightVersionWords.toList)
    compareResult
  }

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

  def shouldUpgrade(currentVersion: String, prospectVersion: String): Boolean = {
    if (isRelease(currentVersion)) {
      if (isRelease(prospectVersion)) {
        if (compare(currentVersion, prospectVersion) == -1) {
          true
        } else {
          false
        }
      } else {
        false
      }
    } else {
      if (compare(currentVersion, prospectVersion) == -1) {
        true
      } else {
        false
      }
    }
  }
}
