package com.seanshubin.up_to_date.logic

case class RecommendedVersionBump(fromVersion: String, maybeToVersion: Option[String])

case class RecommendationBySource(latestRelease: String, location: String, bySource: Map[String, RecommendedVersionBump])

case class Recommendations(totalDependencies: Int,
                           dependenciesToUpgrade: Int,
                           versionEntriesToUpgrade: Int,
                           byGroupAndArtifact: Map[GroupAndArtifact, RecommendationBySource])
