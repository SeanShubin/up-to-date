package com.seanshubin.up_to_date.logic

case class ValidConfiguration(pomDirectories: Seq[String],
                              mavenRepositories: Seq[String],
                              automaticallyUpdate: Seq[Seq[String]],
                              ignore: Seq[Seq[String]],
                              reportDirectory: String,
                              cacheDirectory: String,
                              expireCache: String)

object ValidConfiguration {
  val Empty = ValidConfiguration(Seq(), Seq(), Seq(), Seq(), ".", ".", "0")
}