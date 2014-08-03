package com.seanshubin.up_to_date.logic

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class MetadataParserTest extends FunSuite {
  test("parse") {
    val charset = StandardCharsets.UTF_8
    val metadataParser: MetadataParser = new MetadataParserImpl(charset)
    val actual = metadataParser.parseVersions(SampleData.sampleMetadataContents)
    val expected = Seq(
      "20030203.000550", "2.5-SNAPSHOT", "2.4", "2.3", "2.2", "2.1", "2.0.1", "2.0", "1.4-backport-IO-168", "1.4",
      "1.4", "1.3.2", "1.3.1", "1.3", "1.2", "1.1", "1.0", "0.1")
    assert(actual === expected)
  }
}
