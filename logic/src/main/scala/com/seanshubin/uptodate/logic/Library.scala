package com.seanshubin.uptodate.logic

case class Library(location: String, group: String, artifact: String, versions: Seq[String]) extends Ordered[Library] {
  override def compare(that: Library): Int = {
    Ordering.
      Tuple2(Ordering.String, Ordering.String).
      compare((group, artifact), (that.group, that.artifact))
  }
}

object Library {
  def groupByGroupAndArtifact(libraries: Seq[Library]): Map[GroupAndArtifact, Library] = {
    val entries = for {
      library <- libraries
      Library(location, group, artifact, versions) = library
      groupAndArtifact = GroupAndArtifact(group, artifact)
    } yield {
      (groupAndArtifact, library)
    }
    val map = entries.toMap
    map
  }

  def groupByLocation(libraries: Seq[Library]): Map[String, Seq[Library]] = {
    libraries.groupBy(x => x.location)
  }
}
