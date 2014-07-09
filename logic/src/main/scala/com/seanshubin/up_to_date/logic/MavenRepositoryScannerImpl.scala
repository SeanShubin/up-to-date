package com.seanshubin.up_to_date.logic

class MavenRepositoryScannerImpl(repositories: Seq[String], http: Http, metadataParser: MetadataParser) extends MavenRepositoryScanner {
  override def scanLatestDependencies(groupAndArtifactSet: Set[GroupAndArtifact]): DependencyVersions = {
    val initialDependencyVersions: Map[GroupAndArtifact, LocationAndVersions] = Map()
    val dependencyVersions = groupAndArtifactSet.foldLeft(initialDependencyVersions)(addDependencyVersions)
    DependencyVersions(dependencyVersions)
  }

  private def addDependencyVersions(soFar: Map[GroupAndArtifact, LocationAndVersions],
                                    groupAndArtifact: GroupAndArtifact): Map[GroupAndArtifact, LocationAndVersions] = {
    if (soFar.contains(groupAndArtifact)) {
      soFar
    } else {
      val searchRepository = searchRepositoryForVersions(_: String, groupAndArtifact)
      val maybeVersions = repositories.toStream.map(searchRepository).flatten.headOption
      maybeVersions match {
        case Some((location, versions)) => soFar + (groupAndArtifact -> LocationAndVersions(location, versions))
        case None => soFar
      }
    }
  }

  private def searchRepositoryForVersions(repository: String, groupAndArtifact: GroupAndArtifact): Option[(String, Set[String])] = {
    val url = repository + groupAndArtifact.urlPath + "/maven-metadata.xml"
    val (httpResponseCode, httpContent) = http.get(url)
    if (Http.isSuccess(httpResponseCode)) {
      val versions = metadataParser.parseVersions(httpContent)
      Some(repository -> versions)
    } else {
      None
    }
  }
}
