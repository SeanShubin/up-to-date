package com.seanshubin.up_to_date.logic

case class Observations(existingDependencies: Map[String, Seq[PomDependency]],
                        dependencyVersions: Map[GroupAndArtifact, LocationAndVersions])
