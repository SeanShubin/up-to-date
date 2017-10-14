package com.seanshubin.uptodate.logic
import java.net.URI
import java.nio.file.Path

class NotificationsNotImplemented extends Notifications{
  override def uriSyntaxException(uriString: String): Unit = ???

  override def effectiveConfiguration(configuration: Configuration): Unit = ???

  override def errorWithConfiguration(commandLineArguments: Seq[String], errorReport: Seq[String]): Unit = ???

  override def timeTaken[T](caption: String)(block: => T): T = ???

  override def httpGet(uri: URI): Unit = ???

  override def httpGetFromCache(uri: URI, path: Path): Unit = ???
}
