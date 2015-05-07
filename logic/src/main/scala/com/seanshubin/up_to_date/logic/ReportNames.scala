package com.seanshubin.up_to_date.logic

case class ReportNames(pom: String,
                       repository: String,
                       inconsistency: String,
                       upgradesToApply: String,
                       upgradesToIgnore: String,
                       statusQuo: String,
                       notFound: String,
                       byDependency: String)
