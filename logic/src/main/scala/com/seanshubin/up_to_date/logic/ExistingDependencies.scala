package com.seanshubin.up_to_date.logic

case class ExistingDependencies(byPom: Map[String, Seq[PomDependency]]) {
  def toGroupAndArtifactSet: Set[GroupAndArtifact] = {
    val groupAndArtifactSeq = for {
      (pom, pomDependencySeq) <- byPom
      pomDependency <- pomDependencySeq
    } yield {
      GroupAndArtifact(pomDependency.group, pomDependency.artifact)
    }
    groupAndArtifactSeq.toSet
  }
}
