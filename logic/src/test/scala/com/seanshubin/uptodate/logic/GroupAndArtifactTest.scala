package com.seanshubin.uptodate.logic

import org.scalatest.FunSuite

class GroupAndArtifactTest extends FunSuite {
  test("url path") {
    val groupAndArtifact = GroupAndArtifact("group", "artifact")
    val expected = "/group/artifact"
    val actual = groupAndArtifact.urlPath
    assert(actual === expected)
  }
}
