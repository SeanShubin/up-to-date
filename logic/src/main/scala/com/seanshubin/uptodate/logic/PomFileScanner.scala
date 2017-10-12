package com.seanshubin.uptodate.logic

trait PomFileScanner {
  def scanPomFiles(): Seq[Pom]
}
