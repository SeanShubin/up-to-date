package com.seanshubin.up_to_date.logic

case class DependencyVersions(map: Map[GroupAndArtifact, Set[String]], notFound: Set[GroupAndArtifact]) {
  def addVersions(groupAndArtifact: GroupAndArtifact, versions: Set[String]) = {
    copy(map = map + (groupAndArtifact -> versions))
  }

  def addNotFound(groupAndArtifact: GroupAndArtifact) = {
    copy(notFound = notFound + groupAndArtifact)
  }
}

object DependencyVersions {
  val Empty = DependencyVersions(Map(), Set())
}
