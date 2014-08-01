package com.seanshubin.up_to_date.logic

case class RecommendationPart(group: String,
                              artifact: String,
                              pomLocation: String,
                              pomVersion: String,
                              repositoryLocation: String,
                              repositoryVersions: Set[String])

case class RecommendedVersionBump(fromVersion: String, maybeToVersion: Option[String]) {
  def hasRecommendation: Boolean = maybeToVersion.isDefined
}

case class RecommendationBySource(bestAvailable: String,
                                  repositoryLocation: String,
                                  byPomLocation: Map[String, RecommendedVersionBump]) {
  def hasRecommendation: Boolean = byPomLocation.values.exists(_.hasRecommendation)

  def hasInconsistency: Boolean = byPomLocation.values.map(_.fromVersion).toSet.size > 1

  def versionEntriesToUpgrade: Int = byPomLocation.values.count(_.hasRecommendation)

  def recommendVersionBump(pomLocation: String, pomVersion: Version, versions: Set[Version]) = {
    val maybeUpgrade = pomVersion.selectUpgrade(versions).map(_.originalString)
    val recommendedVersionBump = RecommendedVersionBump(pomVersion.originalString, maybeUpgrade)
    copy(byPomLocation = byPomLocation.updated(pomLocation, recommendedVersionBump))
  }
}

case class Recommendations(byGroupAndArtifact: Map[GroupAndArtifact, RecommendationBySource]) {
  def totalDependencies: Int = byGroupAndArtifact.size

  def dependenciesToUpgrade: Int = byGroupAndArtifact.values.count(_.hasRecommendation)

  def versionEntriesToUpgrade: Int = byGroupAndArtifact.values.map(_.versionEntriesToUpgrade).sum

  def filterWithRecommendation: Map[GroupAndArtifact, RecommendationBySource] =
    for {
      (key, value) <- byGroupAndArtifact
      if value.hasRecommendation
    } yield (key, value)

  def filterWithInconsistent: Map[GroupAndArtifact, RecommendationBySource] =
    for {
      (key, value) <- byGroupAndArtifact
      if value.hasInconsistency
    } yield (key, value)

  def addPart(part: RecommendationPart): Recommendations = {
    val groupAndArtifact = GroupAndArtifact(part.group, part.artifact)
    val repositoryLocation = part.repositoryLocation
    val versions = part.repositoryVersions.map(Version.apply)
    val bestAvailable = Version.bestAvailableVersionFrom(versions)
    val bySource = byGroupAndArtifact.getOrElse(groupAndArtifact, RecommendationBySource(bestAvailable.originalString, repositoryLocation, Map()))
    val pomLocation = part.pomLocation
    val pomVersion = Version(part.pomVersion)
    val newBySource = bySource.recommendVersionBump(pomLocation, pomVersion, versions)
    copy(byGroupAndArtifact = byGroupAndArtifact.updated(groupAndArtifact, newBySource))
  }

  def upgradesByPom: Map[String, Map[GroupAndArtifact, String]] = {
    val rows = for {
      (groupAndArtifact, recommendationBySource) <- byGroupAndArtifact
      (pomLocation, versionBump) <- recommendationBySource.byPomLocation
      RecommendedVersionBump(fromVersion, maybeToVersion) = versionBump
      toVersion <- maybeToVersion
    } yield {
      (pomLocation, groupAndArtifact, toVersion)
    }
    def foldRowIntoMap(map: Map[String, Map[GroupAndArtifact, String]],
                       row: (String, GroupAndArtifact, String)): Map[String, Map[GroupAndArtifact, String]] = {
      val (pomLocation, groupAndArtifact, toVersion) = row
      map.updated(pomLocation, map(pomLocation).updated(groupAndArtifact, toVersion))
    }
    val emptyResult: Map[String, Map[GroupAndArtifact, String]] = Map().withDefaultValue(Map[GroupAndArtifact, String]())
    rows.foldLeft(emptyResult)(foldRowIntoMap)
  }
}

object Recommendations {
  val Empty = Recommendations(Map())
}
