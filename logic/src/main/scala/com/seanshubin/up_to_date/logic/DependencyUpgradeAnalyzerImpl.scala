package com.seanshubin.up_to_date.logic

class DependencyUpgradeAnalyzerImpl extends DependencyUpgradeAnalyzer {
  override def recommendUpgrades(poms: Seq[Pom], libraries: Seq[Library]): Seq[Upgrade] = {
    val dependencies = Pom.toDependencies(poms)
    val libraryByGroupAndArtifact = Library.groupByGroupAndArtifact(libraries)
    for {
      dependency <- dependencies
      groupAndArtifact = dependency.groupAndArtifact
      library <- libraryByGroupAndArtifact.get(groupAndArtifact)
      newVersion <- Version.selectUpgrade(dependency.version, library.versions)
    } yield {
      Upgrade(dependency.location, dependency.group, dependency.artifact, dependency.version, newVersion)
    }
  }

  override def findInconsistencies(poms: Seq[Pom]): Map[GroupAndArtifact, Seq[Dependency]] = {
    val dependencies = Pom.toDependencies(poms)
    val dependenciesByGroupAndArtifact = Dependency.groupByGroupAndArtifact(dependencies)
    dependenciesByGroupAndArtifact.filter(hasInconsistency)
  }

  private def hasInconsistency(entry: (GroupAndArtifact, Seq[Dependency])): Boolean = {
    val (_, dependencies) = entry
    val expectedVersion = dependencies.head.version
    def hasDifferentVersion(dependency: Dependency) = dependency.version != expectedVersion
    val result = dependencies.tail.exists(hasDifferentVersion)
    result
  }
}
