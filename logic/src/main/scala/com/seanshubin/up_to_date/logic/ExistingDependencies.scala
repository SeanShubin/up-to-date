package com.seanshubin.up_to_date.logic

case class ExistingDependencies(dependencies: Set[Dependency]) {
  def toGroupAndArtifactSet: Set[GroupAndArtifact] = {
    for {
      Dependency(location, group, artifact, version) <- dependencies
    } yield {
      GroupAndArtifact(group, artifact)
    }
  }
}
