package com.seanshubin.uptodate.logic

trait PomParser {
  def parseDependencies(pomName: String, pomContents: String): Pom
}
