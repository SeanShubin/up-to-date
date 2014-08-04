package com.seanshubin.up_to_date.logic

case class Dependency(location: String, group: String, artifact: String, version: String) extends Ordered[Dependency] {
  def groupAndArtifact: GroupAndArtifact = GroupAndArtifact(group, artifact)

  override def compare(that: Dependency): Int = Version(this.version).compare(Version(that.version))
}

object Dependency {
  def groupByGroupAndArtifact(dependencies: Seq[Dependency]): Map[GroupAndArtifact, Seq[Dependency]] = {
    dependencies.groupBy(dependency => dependency.groupAndArtifact)
  }
}
