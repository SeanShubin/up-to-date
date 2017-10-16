package com.seanshubin.uptodate.logic

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

  override def alreadyUpToDate(poms: Seq[Pom], libraries: Seq[Library]): Seq[Dependency] = {
    val dependencies = Pom.toDependencies(poms)
    val libraryByGroupAndArtifact = Library.groupByGroupAndArtifact(libraries)
    for {
      dependency <- dependencies
      groupAndArtifact = dependency.groupAndArtifact
      library <- libraryByGroupAndArtifact.get(groupAndArtifact)
      if Version.selectUpgrade(dependency.version, library.versions).isEmpty
    } yield {
      dependency
    }
  }

  override def splitIntoApplyAndIgnore(upgrades: Seq[Upgrade],
                                       doNotUpgradeFrom: Set[GroupAndArtifact],
                                       doNotUpgradeTo: Set[GroupArtifactVersion]): (Seq[Upgrade], Seq[Upgrade]) = {
    def shouldApplyUpgrade(upgrade: Upgrade) = {
      val shouldUpgradeFrom = !doNotUpgradeFrom.contains(upgrade.groupAndArtifact)
      val shouldUpgradeTo = !doNotUpgradeTo.contains(upgrade.groupArtifactVersionTo)
      val result = shouldUpgradeFrom && shouldUpgradeTo
      result
    }

    val result = upgrades.span(shouldApplyUpgrade)
    result
  }

  override def findInconsistencies(poms: Seq[Pom]): Map[GroupAndArtifact, Seq[Dependency]] = {
    val dependencies = Pom.toDependencies(poms)
    val dependenciesByGroupAndArtifact = Dependency.groupByGroupAndArtifact(dependencies)
    dependenciesByGroupAndArtifact.filter(hasInconsistency)
  }

  override def byDependency(upgrades: Seq[Upgrade]): Map[GroupAndArtifact, List[Upgrade]] = {
    def accumulateUpgrade(accumulator: Map[GroupAndArtifact, List[Upgrade]], upgrade: Upgrade): Map[GroupAndArtifact, List[Upgrade]] = {
      val key = upgrade.groupAndArtifact
      val oldValue = accumulator.getOrElse(key, Nil)
      val newValue = upgrade :: oldValue
      accumulator + (key -> newValue)
    }

    val empty = Map[GroupAndArtifact, List[Upgrade]]()
    upgrades.foldLeft(empty)(accumulateUpgrade)
  }

  override def summary(poms: Seq[Pom],
                       upgrades: Map[GroupAndArtifact, List[Upgrade]],
                       notFound: Seq[GroupAndArtifact],
                       apply: Seq[Upgrade],
                       ignore: Seq[Upgrade],
                       alreadyUpToDate: Seq[Dependency],
                       upgradesWereApplied:Boolean): SummaryReport = {
    val totalArtifacts = poms.flatMap(_.dependencies).map(_.groupAndArtifact).toSet.size
    val artifactsToUpgrade = upgrades.size
    val applyCount = apply.map(_.groupAndArtifact).toSet.size
    val ignoreCount = ignore.map(_.groupAndArtifact).toSet.size
    val alreadyUpToDateCount = alreadyUpToDate.map(_.groupAndArtifact).toSet.size
    SummaryReport(totalArtifacts, artifactsToUpgrade, notFound.size, applyCount, ignoreCount, alreadyUpToDateCount, upgradesWereApplied)
  }

  private def hasInconsistency(entry: (GroupAndArtifact, Seq[Dependency])): Boolean = {
    val (_, dependencies) = entry
    val expectedVersion = dependencies.head.version

    def hasDifferentVersion(dependency: Dependency) = dependency.version != expectedVersion

    val result = dependencies.tail.exists(hasDifferentVersion)
    result
  }
}
