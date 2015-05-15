package com.seanshubin.up_to_date.logic

object SampleData {
  def poms(): Seq[Pom] = {
    (1 to 3).map(pom)
  }

  def pom(index: Int): Pom = {
    val location = s"location-$index"
    Pom(s"location-$index", dependencies(index, location), properties(index, 3))
  }

  def properties(index: Int, quantity: Int): Map[String, String] = {
    (for {
      i <- 1 to index
      key = s"substitute-from-$index-$i"
      value = s"substitute-to-$index-$i"
    } yield {
        (key, value)
      }).toMap
  }

  def dependencies(index: Int, location: String): Seq[Dependency] = {
    def dependencyWithLocation(i: Int) = dependency(i, location)
    (1 to 3).map(dependencyWithLocation)
  }

  def dependency(index: Int, location: String): Dependency = {
    Dependency(location, s"group-$index", s"artifact-$index", s"version-$index")
  }

  def libraries(): Seq[Library] = {
    (1 to 3).map(library)
  }

  def library(index: Int): Library = {
    Library(s"location-$index", s"group-$index", s"artifact-$index", versions())
  }

  def versions(): Seq[String] = {
    (1 to 3).map(version)
  }

  def version(index: Int): String = s"version-$index"

  def inconsistencies(): Map[GroupAndArtifact, Seq[Dependency]] = {
    (1 to 3).map(inconsistency).toMap
  }

  def inconsistency(index: Int): (GroupAndArtifact, Seq[Dependency]) = {
    val sampleGroupAndArtifact = groupAndArtifact(index)
    val GroupAndArtifact(group, artifact) = sampleGroupAndArtifact
    val sampleDependencies = dependenciesWithDifferentVersions(group, artifact)
    (sampleGroupAndArtifact, sampleDependencies)
  }

  def groupAndArtifactSeq(): Seq[GroupAndArtifact] = {
    (1 to 3).map(groupAndArtifact)
  }

  def groupAndArtifact(index: Int): GroupAndArtifact = {
    GroupAndArtifact(s"group-$index", s"artifact-$index")
  }

  def dependenciesWithDifferentVersions(group: String, artifact: String): Seq[Dependency] = {
    (1 to 3).map(i => Dependency(s"location-$i", group, artifact, s"version-$i"))
  }

  def upgrades(begin: Int, end: Int): Seq[Upgrade] = {
    (begin to end).map(upgrade)
  }

  def upgrade(index: Int): Upgrade = Upgrade(s"location-$index", s"group-$index", s"artifact-$index", s"fromVersion-$index", s"toVersion-$index")
}
