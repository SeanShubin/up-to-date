package com.seanshubin.up_to_date.logic

case class GroupAndArtifact(group: String, artifact: String) {
  def urlPath: String = "/" + dotToSlash(group) + "/" + artifact

  private def dotToSlash(s: String): String = s.replaceAll("\\.", "/")
}
