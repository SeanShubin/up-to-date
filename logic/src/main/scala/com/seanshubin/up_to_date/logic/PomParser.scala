package com.seanshubin.up_to_date.logic

trait PomParser {
  def parseDependencies(pomName: String, pomContents: String): Pom
}
