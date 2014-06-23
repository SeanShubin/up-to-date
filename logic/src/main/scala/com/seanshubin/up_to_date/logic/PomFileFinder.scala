package com.seanshubin.up_to_date.logic

import java.nio.file.Path

trait PomFileFinder {
  def relevantPomFiles(): Seq[Path]
}
