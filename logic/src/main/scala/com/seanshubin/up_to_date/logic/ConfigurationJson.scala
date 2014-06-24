package com.seanshubin.up_to_date.logic

case class ConfigurationJson(pomFileName: Option[String],
                             directoriesToSearch: Seq[String],
                             directoryNamesToSkip: Seq[String],
                             mavenRepositories: Seq[String],
                             automaticallyUpdate: Seq[Seq[String]],
                             ignore: Seq[Seq[String]],
                             reportDirectory: Option[String],
                             cacheDirectory: Option[String],
                             cacheExpire: Option[String]) {
  def validate(): Either[Seq[String], ValidConfiguration] = {
    (pomFileName, reportDirectory, cacheDirectory, cacheExpire) match {
      case (Some(pomFile), Some(report), Some(cache), Some(expire)) =>
        try {
          val expireMilliseconds = DurationFormat.MillisecondsFormat.parse(expire)
          Right(ValidConfiguration(
            pomFile,
            directoriesToSearch,
            directoryNamesToSkip,
            mavenRepositories,
            automaticallyUpdate,
            ignore,
            report,
            cache,
            expireMilliseconds))
        } catch {
          case ex: RuntimeException => Left(Seq("unable to parse milliseconds for cacheExpire: " + ex.getMessage))
        }
      case (a, b, c, d) =>
        Left(
          errorIfMissing(a, "pomFileName must be specified") ++
            errorIfMissing(b, "reportDirectory must be specified") ++
            errorIfMissing(c, "cacheDirectory must be specified") ++
            errorIfMissing(d, "cacheExpire must be specified"))
    }
  }

  private def errorIfMissing(s: Option[String], message: String): Seq[String] = {
    s match {
      case Some(_) => Seq()
      case None => Seq(message)
    }
  }
}
