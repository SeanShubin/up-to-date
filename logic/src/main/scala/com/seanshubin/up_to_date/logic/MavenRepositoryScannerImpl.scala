package com.seanshubin.up_to_date.logic

class MavenRepositoryScannerImpl(repositories: Seq[String], http: Http, metadataParser: MetadataParser) extends MavenRepositoryScanner {
  override def scanLatestDependencies(poms: Seq[Pom]): (Seq[Library], Seq[GroupAndArtifact]) = {
    val groupAndArtifactSet = Pom.toGroupAndArtifactSet(poms)
    val foundAndNotFound: Seq[Either[GroupAndArtifact, Library]] = groupAndArtifactSet.toSeq.map(searchRepositoriesForVersions)
    val found: Seq[Library] = foundAndNotFound.map(EitherUtil.extractRight).flatten.sorted
    val notFound = foundAndNotFound.map(EitherUtil.extractLeft).flatten.sorted
    (found, notFound)
  }

  private def searchRepositoriesForVersions(groupAndArtifact: GroupAndArtifact): Either[GroupAndArtifact, Library] = {
    def searchRepository(repository: String) = searchRepositoryForVersions(repository, groupAndArtifact)
    EitherUtil.firstRight(repositories, searchRepository)
  }

  private def searchRepositoryForVersions(repository: String,
                                          groupAndArtifact: GroupAndArtifact): Either[GroupAndArtifact, Library] = {
    val url = repository + groupAndArtifact.urlPath + "/maven-metadata.xml"
    val (httpResponseCode, httpContent) = http.get(url)
    if (Http.isSuccess(httpResponseCode)) {
      val versions = metadataParser.parseVersions(httpContent)
      val GroupAndArtifact(group, artifact) = groupAndArtifact
      val library = Library(url, group, artifact, versions)
      Right(library)
    } else {
      Left(groupAndArtifact)
    }
  }
}
