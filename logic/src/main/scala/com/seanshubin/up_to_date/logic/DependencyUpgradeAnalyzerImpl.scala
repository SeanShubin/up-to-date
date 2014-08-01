package com.seanshubin.up_to_date.logic

class DependencyUpgradeAnalyzerImpl extends DependencyUpgradeAnalyzer {
  override def recommend(existingDependencies: ExistingDependencies, latestDependencies: DependencyVersions): Recommendations = {
    val parts = for {
      (pomLocation, pomDependencies) <- existingDependencies.byPom
      PomDependency(group, artifact, pomVersion) <- pomDependencies
      groupAndArtifact = GroupAndArtifact(group, artifact)
      LocationAndVersions(repositoryLocation, repositoryVersions) <- latestDependencies.map.get(groupAndArtifact)
    } yield {
      RecommendationPart(group, artifact, pomLocation, pomVersion, repositoryLocation, repositoryVersions)
    }
    parts.foldLeft(Recommendations.Empty)(addDependency)
  }

  private def addDependency(recommendations: Recommendations, part: RecommendationPart): Recommendations = {
    recommendations.addPart(part)
  }
}
