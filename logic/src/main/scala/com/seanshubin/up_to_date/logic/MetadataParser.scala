package com.seanshubin.up_to_date.logic

trait MetadataParser {
  def parseVersions(content: String): Seq[String]
}
