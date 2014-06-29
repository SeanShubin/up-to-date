package com.seanshubin.up_to_date.logic

trait MavenRepositoryScanner {
  def scanLatestDependencies(groupAndArtifactSet: Set[GroupAndArtifact]): DependencyVersions
}
