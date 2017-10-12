package com.seanshubin.uptodate.logic

trait MavenRepositoryScanner {
  def scanLatestDependencies(poms: Seq[Pom]): (Seq[Library], Seq[GroupAndArtifact])
}
