package com.seanshubin.up_to_date.logic

trait MavenRepositoryScanner {
  def scanLatestDependencies(poms: Seq[Pom]): Seq[Library]
}
