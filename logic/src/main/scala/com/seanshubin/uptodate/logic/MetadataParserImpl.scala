package com.seanshubin.uptodate.logic

import java.nio.charset.Charset

class MetadataParserImpl(charset: Charset) extends MetadataParser {
  override def parseVersions(content: String): Seq[String] = {
    val document = DocumentUtil.stringToDocument(content, charset)
    val nodeList = document.getElementsByTagName("version")
    val elements = DocumentUtil.nodeListToTraversable(nodeList)
    val versionStrings = elements.map(_.getTextContent)
    versionStrings.toSeq.sortWith(Version.stringDescending)
  }
}
