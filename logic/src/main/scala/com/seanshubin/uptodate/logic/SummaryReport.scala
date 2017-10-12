package com.seanshubin.uptodate.logic

case class SummaryReport(totalArtifacts: Int,
                         artifactsToUpgrade: Int,
                         notFound: Int,
                         apply: Int,
                         ignore: Int,
                         alreadyUpToDateCount: Int)
