package com.seanshubin.up_to_date.logic

case class Dependency(id: String, group: String, artifact: String, version: String) extends Ordered[Dependency] {
  override def compare(that: Dependency): Int = this.version.compare(that.version)
}

object Dependency {
  private val VersionInArtifactId = """(.*)_(.*)""".r

  def create(group: String, artifact: String, version: String) =
    artifact match {
      case VersionInArtifactId(artifactWord, versionWord) =>
        Dependency(group + "." + artifactWord, group: String, artifact: String, version: String)
      case _ =>
        Dependency(group + "." + artifact, group: String, artifact: String, version: String)
    }
}
