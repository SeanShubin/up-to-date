package com.seanshubin.up_to_date.logic

case class SummaryReport(totalArtifacts: Int,
                         artifactsToUpgrade: Int,
                         notFound: Int,
                         apply: Int,
                         ignore: Int,
                         alreadyUpToDateCount: Int)
