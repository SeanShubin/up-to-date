package com.seanshubin.up_to_date.logic

class MavenRepositoryScannerImpl(repositories: Seq[String], http: Http, metadataParser: MetadataParser) extends MavenRepositoryScanner {
  override def scanLatestDependencies(poms: Seq[Pom]): Seq[Library] = {
    val groupAndArtifactSet = Pom.toGroupAndArtifactSet(poms)
    groupAndArtifactSet.toSeq.flatMap(searchRepositoriesForVersions).sorted
  }

  private def searchRepositoriesForVersions(groupAndArtifact: GroupAndArtifact): Option[Library] = {
    def searchRepository(repository: String) = searchRepositoryForVersions(repository, groupAndArtifact)
    repositories.toStream.map(searchRepository).flatten.headOption
  }

  private def searchRepositoryForVersions(repository: String, groupAndArtifact: GroupAndArtifact): Option[Library] = {
    val url = repository + groupAndArtifact.urlPath + "/maven-metadata.xml"
    val (httpResponseCode, httpContent) = http.get(url)
    if (Http.isSuccess(httpResponseCode)) {
      val versions = metadataParser.parseVersions(httpContent)
      val GroupAndArtifact(group, artifact) = groupAndArtifact
      val library = Library(url, group, artifact, versions)
      Some(library)
    } else {
      None
    }
  }
}
