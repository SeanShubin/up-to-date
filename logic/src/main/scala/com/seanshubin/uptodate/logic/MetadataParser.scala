package com.seanshubin.uptodate.logic

trait MetadataParser {
  def parseVersions(content: String): Seq[String]
}
