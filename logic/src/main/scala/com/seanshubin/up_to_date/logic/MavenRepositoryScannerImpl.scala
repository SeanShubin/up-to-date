package com.seanshubin.up_to_date.logic

import java.net.{URI, URISyntaxException}

class MavenRepositoryScannerImpl(repositories: Seq[String],
                                 http: Http,
                                 metadataParser: MetadataParser,
                                 notifications: Notifications) extends MavenRepositoryScanner {
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
    val uriString = repository + groupAndArtifact.urlPath + "/maven-metadata.xml"
    try {
      val uri = new URI(uriString)
      val (httpResponseCode, httpContent) = http.get(uri)
      if (Http.isSuccess(httpResponseCode)) {
        val versions = metadataParser.parseVersions(httpContent)
        val GroupAndArtifact(group, artifact) = groupAndArtifact
        val library = Library(uriString, group, artifact, versions)
        Right(library)
      } else {
        Left(groupAndArtifact)
      }
    } catch {
      case ex: URISyntaxException =>
        notifications.uriSyntaxException(uriString)
        Left(groupAndArtifact)
    }
  }
}
