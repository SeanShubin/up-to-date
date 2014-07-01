package com.seanshubin.up_to_date.logic

import java.nio.charset.Charset

class MetadataParserImpl(charset: Charset) extends MetadataParser {
  override def parseVersions(content: String): Set[String] = {
    val document = DocumentUtil.stringToDocument(content, charset)
    val nodeList = document.getElementsByTagName("version")
    val elements = DocumentUtil.nodeListToTraversable(nodeList)
    val versionStrings = elements.map(_.getTextContent)
    versionStrings.toSet
  }
}
