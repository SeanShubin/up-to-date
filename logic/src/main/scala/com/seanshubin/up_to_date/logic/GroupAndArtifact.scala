package com.seanshubin.up_to_date.logic

case class GroupAndArtifact(group: String, artifact: String) extends Ordered[GroupAndArtifact] {
  def urlPath: String = "/" + dotToSlash(group) + "/" + artifact

  private def dotToSlash(s: String): String = s.replaceAll("\\.", "/")

  def addVersion(version: String) = GroupArtifactVersion(group, artifact, version)

  override def compare(that: GroupAndArtifact): Int = {
    Ordering.Tuple2(Ordering.String, Ordering.String).compare((group, artifact), (that.group, that.artifact))
  }
}
