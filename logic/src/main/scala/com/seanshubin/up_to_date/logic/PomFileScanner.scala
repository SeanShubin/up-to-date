package com.seanshubin.up_to_date.logic

trait PomFileScanner {
  def scanExistingDependencies(): ExistingDependencies
}
