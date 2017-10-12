package com.seanshubin.uptodate.logic

import java.net.URI

import org.scalatest.FunSuite
import org.scalatest.easymock.EasyMockSugar

class MavenRepositoryScannerTest extends FunSuite with EasyMockSugar {
  test("scan latest dependencies") {
    val repositories = Seq("repo1")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val notifications = mock[Notifications]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser, notifications)
    val dependency1 = Dependency("pom.xml", "group1", "artifact1", "version1")
    val dependency2 = Dependency("pom.xml", "group2", "artifact2", "version1")

    val pom = Pom("pom.xml", Seq(dependency1, dependency2), properties)
    val poms = Seq(pom)
    val versions1 = Seq("version1a", "version1b", "version1c")
    val versions2 = Seq("version2a", "version2b", "version2c")
    val expectedLibraries = Seq(
      Library("repo1/group1/artifact1/maven-metadata.xml", "group1", "artifact1", versions1),
      Library("repo1/group2/artifact2/maven-metadata.xml", "group2", "artifact2", versions2)
    )
    val expectedNotFound: Seq[GroupAndArtifact] = Seq()
    val expected = (expectedLibraries, expectedNotFound)
    expecting {
      http.get(new URI("repo1/group1/artifact1/maven-metadata.xml")).andReturn((200, "content1"))
      metadataParser.parseVersions("content1").andReturn(versions1)
      http.get(new URI("repo1/group2/artifact2/maven-metadata.xml")).andReturn((200, "content2"))
      metadataParser.parseVersions("content2").andReturn(versions2)
    }
    whenExecuting(http, metadataParser, notifications) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(poms)
      assert(actual === expected)
    }
  }

  test("don't search later repository if earlier repository finds it") {
    val repositories = Seq("repo1", "repo2")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val notifications = mock[Notifications]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser, notifications)
    val dependency = Dependency("pom.xml", "group", "artifact", "version1")
    val pom = Pom("pom.xml", Seq(dependency), properties)
    val poms = Seq(pom)
    val versions = Seq("version-a", "version-b", "version-c")
    val expectedLibraries = Seq(Library("repo1/group/artifact/maven-metadata.xml", "group", "artifact", versions))
    val expectedNotFound: Seq[GroupAndArtifact] = Seq()
    val expected = (expectedLibraries, expectedNotFound)
    expecting {
      http.get(new URI("repo1/group/artifact/maven-metadata.xml")).andReturn((200, "content"))
      metadataParser.parseVersions("content").andReturn(versions)
    }
    whenExecuting(http, metadataParser, notifications) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(poms)
      assert(actual === expected)
    }
  }

  test("search later repository if earlier repository does not find it") {
    val repositories = Seq("repo1", "repo2")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val notifications = mock[Notifications]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser, notifications)
    val dependency = Dependency("pom.xml", "group", "artifact", "version1")
    val pom = Pom("pom.xml", Seq(dependency), properties)
    val poms = Seq(pom)
    val versions = Seq("version-a", "version-b", "version-c")
    val expectedLibraries = Seq(Library("repo2/group/artifact/maven-metadata.xml", "group", "artifact", versions))
    val expectedNotFound: Seq[GroupAndArtifact] = Seq()
    val expected = (expectedLibraries, expectedNotFound)
    expecting {
      http.get(new URI("repo1/group/artifact/maven-metadata.xml")).andReturn((404, "not found"))
      http.get(new URI("repo2/group/artifact/maven-metadata.xml")).andReturn((200, "content"))
      metadataParser.parseVersions("content").andReturn(versions)
    }
    whenExecuting(http, metadataParser, notifications) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(poms)
      assert(actual === expected)
    }
  }

  test("dependency not found") {
    val repositories = Seq("repo1", "repo2")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val notifications = mock[Notifications]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser, notifications)
    val dependency = Dependency("pom.xml", "group", "artifact", "version1")
    val pom = Pom("pom.xml", Seq(dependency), properties)
    val poms = Seq(pom)
    val expectedLibraries: Seq[Library] = Seq()
    val expectedNotFound: Seq[GroupAndArtifact] = Seq(GroupAndArtifact("group", "artifact"))
    val expected = (expectedLibraries, expectedNotFound)
    expecting {
      http.get(new URI("repo1/group/artifact/maven-metadata.xml")).andReturn((404, "not found"))
      http.get(new URI("repo2/group/artifact/maven-metadata.xml")).andReturn((404, "not found"))
    }
    whenExecuting(http, metadataParser, notifications) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(poms)
      assert(actual === expected)
    }
  }

  val properties = Map(
    """'\$\{scala\.major\}'""" -> "2.11",
    """'\$\{scala\.major\.minor\}'""" -> "2.11.6",
    """'\$\{scala\.version\}'""" -> "2.11",
    """'\$\{scala\.compat\.version\}'""" -> "2.11"
  )
}
