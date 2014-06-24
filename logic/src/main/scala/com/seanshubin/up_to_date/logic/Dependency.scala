package com.seanshubin.up_to_date.logic

case class Dependency(group: String, artifact: String, version: String) extends Ordered[Dependency] {
  def id: String = artifact match {
    case Dependency.VersionInArtifactId(artifactWord, versionWord) => group + "." + artifactWord
    case _ => group + "." + artifact
  }

  override def compare(that: Dependency): Int = this.version.compare(that.version)
}

object Dependency {
  private val VersionInArtifactId = """(.*)_(.*)""".r
}
