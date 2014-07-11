package com.seanshubin.up_to_date.logic

case class OutOfDateReport(byGroupAndArtifact: Map[GroupAndArtifact, OutOfDate])
