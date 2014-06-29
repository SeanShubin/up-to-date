package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite
import org.scalatest.mock.EasyMockSugar

class MavenRepositoryScannerTest extends FunSuite with EasyMockSugar {
  test("scan latest dependencies") {
    val repositories = Seq("repo1")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser)
    val dependency1 = GroupAndArtifact("group1", "artifact1")
    val dependency2 = GroupAndArtifact("group2", "artifact2")
    val groupAndArtifactSet = Set(dependency1, dependency2)
    val expected = DependencyVersions(
      map = Map(
        dependency1 -> Set("version1a", "version1b", "version1c"),
        dependency2 -> Set("version2a", "version2b", "version2c")),
      notFound = Set()
    )
    expecting {
      http.get("repo1/group1/artifact1/maven-metadata.xml").andReturn((200, "content1"))
      http.get("repo1/group2/artifact2/maven-metadata.xml").andReturn((200, "content2"))
      metadataParser.parseVersions("content1").andReturn(Set("version1a", "version1b", "version1c"))
      metadataParser.parseVersions("content2").andReturn(Set("version2a", "version2b", "version2c"))
    }
    whenExecuting(http, metadataParser) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(groupAndArtifactSet)
      assert(actual === expected)
    }
  }
  test("don't search later repository if earlier repository finds it") {
    val repositories = Seq("repo1", "repo2")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser)
    val dependency = GroupAndArtifact("group", "artifact")
    val groupAndArtifactSet = Set(dependency)
    val expected = DependencyVersions(
      map = Map(dependency -> Set("version-a", "version-b", "version-c")),
      notFound = Set()
    )
    expecting {
      http.get("repo1/group/artifact/maven-metadata.xml").andReturn((200, "content"))
      metadataParser.parseVersions("content").andReturn(Set("version-a", "version-b", "version-c"))
    }
    whenExecuting(http, metadataParser) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(groupAndArtifactSet)
      assert(actual === expected)
    }
  }
  test("search later repository if earlier repository does not find it") {
    val repositories = Seq("repo1", "repo2")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser)
    val dependency = GroupAndArtifact("group", "artifact")
    val groupAndArtifactSet = Set(dependency)
    val expected = DependencyVersions(
      map = Map(dependency -> Set("version-a", "version-b", "version-c")),
      notFound = Set()
    )
    expecting {
      http.get("repo1/group/artifact/maven-metadata.xml").andReturn((404, "not found"))
      http.get("repo2/group/artifact/maven-metadata.xml").andReturn((200, "content"))
      metadataParser.parseVersions("content").andReturn(Set("version-a", "version-b", "version-c"))
    }
    whenExecuting(http, metadataParser) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(groupAndArtifactSet)
      assert(actual === expected)
    }
  }
  test("dependency not found") {
    val repositories = Seq("repo1", "repo2")
    val http = mock[Http]
    val metadataParser = mock[MetadataParser]
    val mavenRepositoryScanner: MavenRepositoryScanner =
      new MavenRepositoryScannerImpl(repositories, http, metadataParser)
    val dependency = GroupAndArtifact("group", "artifact")
    val groupAndArtifactSet = Set(dependency)
    val expected = DependencyVersions(
      map = Map(),
      notFound = groupAndArtifactSet
    )
    expecting {
      http.get("repo1/group/artifact/maven-metadata.xml").andReturn((404, "not found"))
      http.get("repo2/group/artifact/maven-metadata.xml").andReturn((404, "not found"))
    }
    whenExecuting(http, metadataParser) {
      val actual = mavenRepositoryScanner.scanLatestDependencies(groupAndArtifactSet)
      assert(actual === expected)
    }
  }
}
