package com.seanshubin.up_to_date.logic

case class RecommendationPart(group: String,
                              artifact: String,
                              pomLocation: String,
                              pomVersion: String,
                              repositoryLocation: String,
                              repositoryVersions: Set[String])

case class RecommendedVersionBump(fromVersion: String, maybeToVersion: Option[String])

case class RecommendationBySource(bestAvailable: String,
                                  repositoryLocation: String,
                                  byPomLocation: Map[String, RecommendedVersionBump]) {
  def recommendVersionBump(pomLocation: String, pomVersion: Version, versions: Set[Version]) = {
    val maybeUpgrade = pomVersion.selectUpgrade(versions).map(_.originalString)
    val recommendedVersionBump = RecommendedVersionBump(pomVersion.originalString, maybeUpgrade)
    copy(byPomLocation = byPomLocation.updated(pomLocation, recommendedVersionBump))
  }
}

case class Recommendations(totalDependencies: Int,
                           dependenciesToUpgrade: Int,
                           versionEntriesToUpgrade: Int,
                           byGroupAndArtifact: Map[GroupAndArtifact, RecommendationBySource]) {
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
}

object Recommendations {
  val Empty = Recommendations(0, 0, 0, Map())
}
