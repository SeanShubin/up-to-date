package com.seanshubin.up_to_date.logic

class MavenRepositoryScannerImpl(repositories: Seq[String], http: Http, metadataParser: MetadataParser) extends MavenRepositoryScanner {
  override def scanLatestDependencies(groupAndArtifactSet: Set[GroupAndArtifact]): DependencyVersions = {
    val initialDependencyVersions = DependencyVersions.Empty
    val dependencyVersions = groupAndArtifactSet.foldLeft(initialDependencyVersions)(addDependencyVersions)
    dependencyVersions
  }

  private def addDependencyVersions(soFar: DependencyVersions, groupAndArtifact: GroupAndArtifact): DependencyVersions = {
    if (soFar.map.contains(groupAndArtifact)) {
      soFar
    } else {
      val searchRepository = searchRepositoryForVersions(_: String, groupAndArtifact)
      val maybeVersions = repositories.toStream.map(searchRepository).flatten.headOption
      maybeVersions match {
        case Some(versions) => soFar.addVersions(groupAndArtifact, versions)
        case None => soFar.addNotFound(groupAndArtifact)
      }
    }
  }

  private def searchRepositoryForVersions(repository: String, groupAndArtifact: GroupAndArtifact): Option[Set[String]] = {
    val url = repository + groupAndArtifact.urlPath + "/maven-metadata.xml"
    val (httpResponseCode, httpContent) = http.get(url)
    if (Http.isSuccess(httpResponseCode)) {
      val versions = metadataParser.parseVersions(httpContent)
      Some(versions)
    } else {
      None
    }
  }
}
