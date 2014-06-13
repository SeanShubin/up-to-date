package com.seanshubin.up_to_date.logic

case class ConfigurationJson(pomDirectories: Seq[String],
                             mavenRepositories: Seq[String],
                             automaticallyUpdate: Seq[Seq[String]],
                             ignore: Seq[Seq[String]],
                             reportDirectory: Option[String],
                             cacheDirectory: Option[String],
                             cacheExpire: Option[String]) {
  def validate(): Either[Seq[String], ValidConfiguration] = {
    (reportDirectory, cacheDirectory, cacheExpire) match {
      case (Some(report), Some(cache), Some(expire)) =>
        try {
          val expireMilliseconds = DurationFormat.MillisecondsFormat.parse(expire)
          Right(ValidConfiguration(pomDirectories, mavenRepositories, automaticallyUpdate, ignore, report, cache, expireMilliseconds))
        } catch {
          case ex: RuntimeException => Left(Seq("unable to parse milliseconds for cacheExpire: " + ex.getMessage))
        }
      case (x, y, z) =>
        Left(errorIfMissing(x, "reportDirectory must be specified") ++
          errorIfMissing(y, "cacheDirectory must be specified") ++
          errorIfMissing(z, "cacheExpire must be specified"))
    }
  }

  private def errorIfMissing(s: Option[String], message: String): Seq[String] = {
    s match {
      case Some(_) => Seq()
      case None => Seq(message)
    }
  }
}
