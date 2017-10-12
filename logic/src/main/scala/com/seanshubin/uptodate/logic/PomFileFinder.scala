package com.seanshubin.uptodate.logic

import java.nio.file.Path

trait PomFileFinder {
  def relevantPomFiles(): Seq[Path]
}
