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
                                  byPomLocation: Map[String, RecommendedVersionBump])

case class Recommendations(totalDependencies: Int,
                           dependenciesToUpgrade: Int,
                           versionEntriesToUpgrade: Int,
                           byGroupAndArtifact: Map[GroupAndArtifact, RecommendationBySource]) {
  def addPart(part:RecommendationPart): Recommendations = {
    ???
  }
}

object Recommendations {
  val Empty = Recommendations(0, 0, 0, Map())
}
