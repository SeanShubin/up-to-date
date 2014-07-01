package com.seanshubin.up_to_date.logic

import java.nio.charset.StandardCharsets

import org.scalatest.FunSuite

class MetadataParserTest extends FunSuite {
  test("parse") {
    val charset = StandardCharsets.UTF_8
    val metadataParser: MetadataParser = new MetadataParserImpl(charset)
    val actual = metadataParser.parseVersions(SampleData.sampleMetadataContents)
    val expected = Set(
      "0.1", "1.0", "1.1", "1.2", "1.3", "1.3.1", "1.3.2", "1.4",
      "1.4-backport-IO-168", "2.0", "2.0.1", "2.1", "2.2",
      "2.3", "2.4", "2.5-SNAPSHOT", "20030203.000550")
    assert(actual === expected)
  }
}
