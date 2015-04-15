package com.seanshubin.up_to_date.logic

case class Pom(location: String, dependencies: Seq[Dependency])

object Pom {
  def toDependencies(poms: Seq[Pom]): Seq[Dependency] = {
    for {
      pom <- poms
      dependency <- pom.dependencies
    } yield {
      dependency
    }
  }

  def groupByLocation(poms: Seq[Pom]): Map[String, Pom] = {
    def toEntry(pom: Pom) = (pom.location, pom)
    poms.map(toEntry).toMap
  }

  def toGroupAndArtifactSet(poms: Seq[Pom]): Set[GroupAndArtifact] = {
    val groupAndArtifact = for {
      pom <- poms
      Pom(location, dependencies) = pom
      dependency <- dependencies
    } yield {
        GroupAndArtifact(dependency.group, dependency.artifact)
      }
    groupAndArtifact.toSet
  }
}
