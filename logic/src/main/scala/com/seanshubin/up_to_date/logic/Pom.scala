package com.seanshubin.up_to_date.logic

case class Pom(location: String, dependencies: Seq[Dependency], properties: Map[String, String])

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
      Pom(location, dependencies, properties) = pom
      dependency <- dependencies
    } yield {
      GroupAndArtifact(dependency.group, dependency.artifact)
    }
    groupAndArtifact.toSet
  }

  def applyExpansions(poms: Seq[Pom]): (Seq[Pom], Map[String, Set[String]]) = {
    val combineResult = PropertyUtil.combine(poms.map(_.properties): _*)
    val expanded = poms.map(pom => applySpecifiedExpansions(pom, combineResult.merged))
    (expanded, combineResult.conflict)
  }

  private def applySpecifiedExpansions(pom: Pom, properties: Map[String, String]): Pom = {
    def updateDependency(dependency: Dependency): Dependency = {
      dependency.copy(
        group = PropertyUtil.expand(dependency.group, properties),
        artifact = PropertyUtil.expand(dependency.artifact, properties),
        version = PropertyUtil.expand(dependency.version, properties)
      )
    }

    val expandedDependencies = pom.dependencies.map(updateDependency)
    pom.copy(dependencies = expandedDependencies)
  }
}
