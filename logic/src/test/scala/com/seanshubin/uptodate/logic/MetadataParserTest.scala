package com.seanshubin.uptodate.logic

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class MetadataParserTest extends FunSuite {
  test("parse") {
    val charset = StandardCharsets.UTF_8
    val metadataParser: MetadataParser = new MetadataParserImpl(charset)
    val actual = metadataParser.parseVersions(sampleMetadataContents)
    val expected = Seq(
      "20030203.000550", "2.5-SNAPSHOT", "2.4", "2.3", "2.2", "2.1", "2.0.1", "2.0", "1.4-backport-IO-168", "1.4",
      "1.4", "1.3.2", "1.3.1", "1.3", "1.2", "1.1", "1.0", "0.1")
    assert(actual === expected)
  }

  val sampleMetadataContents =
    """<?xml version="1.0" encoding="UTF-8"?>
      |<metadata modelVersion="1.1.0">
      |  <groupId>commons-io</groupId>
      |  <artifactId>commons-io</artifactId>
      |  <version>1.4</version>
      |  <versioning>
      |    <latest>20030203.000550</latest>
      |    <release>20030203.000550</release>
      |    <versions>
      |      <version>0.1</version>
      |      <version>1.0</version>
      |      <version>1.1</version>
      |      <version>1.2</version>
      |      <version>1.3</version>
      |      <version>1.3.1</version>
      |      <version>1.3.2</version>
      |      <version>1.4</version>
      |      <version>1.4-backport-IO-168</version>
      |      <version>2.0</version>
      |      <version>2.0.1</version>
      |      <version>2.1</version>
      |      <version>2.2</version>
      |      <version>2.3</version>
      |      <version>2.4</version>
      |      <version>2.5-SNAPSHOT</version>
      |      <version>20030203.000550</version>
      |    </versions>
      |    <lastUpdated>20140623122436</lastUpdated>
      |  </versioning>
      |</metadata>
      | """.stripMargin
}
