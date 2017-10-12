package com.seanshubin.uptodate.logic

case class ReportNames(pom: String,
                       repository: String,
                       inconsistency: String,
                       upgradesToApply: String,
                       upgradesToIgnore: String,
                       statusQuo: String,
                       notFound: String,
                       byDependency: String,
                       summary: String,
                       unexpandedPom: String,
                       propertyConflict: String)
