package com.seanshubin.up_to_date.logic

import java.nio.file.Path

trait PomParser {
  def parseDependencies(path: Path): (String, Seq[PomDependency])
}
