package com.seanshubin.uptodate.logic

case class GroupAndArtifact(group: String, artifact: String) extends Ordered[GroupAndArtifact] {
  def urlPath: String = "/" + dotToSlash(group) + "/" + artifact

  private def dotToSlash(s: String): String = s.replaceAll("\\.", "/")

  override def compare(that: GroupAndArtifact): Int = {
    Ordering.Tuple2(Ordering.String, Ordering.String).compare((group, artifact), (that.group, that.artifact))
  }
}
