package com.seanshubin.uptodate.logic

import java.net.URI
import java.nio.file.{Path, Paths}

import com.seanshubin.uptodate.logic.Datum.{DatumInt, DatumUtf}
import org.scalatest.FunSuite

import scala.collection.mutable.ArrayBuffer

class HttpCacheTest extends FunSuite {
  test("load from filesystem when in cache and not expired") {
    // given
    val uriString: String = "http://localhost:12345/foo"
    val uri: URI = new URI(uriString)
    val cacheFileName = "hashed-uri"
    val expireCache = DurationFormat.MillisecondsFormat.parse("5 days")
    val cacheDirectoryName = "cache"
    val cacheDirectory = Paths.get(cacheDirectoryName)
    val cacheFilePath = Paths.get(cacheDirectoryName, cacheFileName)
    val oneDayAgo = 1000
    val oneDay = DurationFormat.MillisecondsFormat.parse("1 day")
    val rightNow = oneDayAgo + oneDay
    val dataInput = new DataInputStreamWrapperStub(DatumInt(200), DatumUtf("""{ "result": "ok" }"""))
    val cacheFileInfo = new FileInfo(oneDayAgo, dataInput, null)
    val delegate = new HttpStub
    val oneWayHash = new OneWayHashStub(uriString -> cacheFileName)
    val fileSystem = new FileSystemStub(Set(cacheFilePath), Map(cacheFilePath -> cacheFileInfo))
    val systemClock = new SystemClockStub(rightNow)
    val notifications = new NotificationsStub
    val http: Http = new HttpCache(delegate, oneWayHash, fileSystem, cacheDirectory, expireCache, systemClock, notifications)

    // when
    val (actualStatusCode, actualContent) = http.get(uri)

    // then
    assert(actualStatusCode === 200)
    assert(actualContent === """{ "result": "ok" }""")
    val expectedUri = new URI("http://localhost:12345/foo")
    val expectedPath = Paths.get("cache/hashed-uri")
    assert(notifications.httpGetFromCacheInvocations.size === 1)
    assert(notifications.httpGetFromCacheInvocations(0).uri === expectedUri)
    assert(notifications.httpGetFromCacheInvocations(0).path === expectedPath)
    assert(dataInput.closed === true)
  }

  test("load from uri and update cache when in cache and expired") {
    // given
    val uriString: String = "http://localhost:12345/foo"
    val uri: URI = new URI(uriString)
    val cacheFileName = "hashed-uri"
    val expireCache = DurationFormat.MillisecondsFormat.parse("5 days")
    val cacheDirectoryName = "cache"
    val cacheDirectory = Paths.get(cacheDirectoryName)
    val cacheFilePath = Paths.get(cacheDirectoryName, cacheFileName)
    val sixDaysAgo = 1000
    val sixDays = DurationFormat.MillisecondsFormat.parse("6 days")
    val rightNow = sixDaysAgo + sixDays
    val delegate = new HttpStub(uri -> (200, """{ "result": "ok" }"""))
    val oneWayHash = new OneWayHashStub(uriString -> cacheFileName)
    val dataOutput = new DataOutputStreamWrapperStub()
    val cacheFileInfo = new FileInfo(sixDaysAgo, null, dataOutput)
    val fileSystem = new FileSystemStub(Set(cacheFilePath), Map(cacheFilePath -> cacheFileInfo))
    val systemClock = new SystemClockStub(rightNow)
    val notifications: Notifications = null
    val http: Http = new HttpCache(delegate, oneWayHash, fileSystem, cacheDirectory, expireCache, systemClock, notifications)

    // when
    val (actualStatusCode, actualContent) = http.get(uri)

    // then
    assert(actualStatusCode === 200)
    assert(actualContent === """{ "result": "ok" }""")
    assert(dataOutput.written.size === 2)
    assert(dataOutput.written(0) === DatumInt(200))
    assert(dataOutput.written(1) === DatumUtf("""{ "result": "ok" }"""))
    assert(dataOutput.closed === true)
  }


  test("load from uri and update cache when not in cache") {
    // given
    val uriString: String = "http://localhost:12345/foo"
    val uri: URI = new URI(uriString)
    val cacheFileName = "hashed-uri"
    val expireCache = DurationFormat.MillisecondsFormat.parse("5 days")
    val cacheDirectoryName = "cache"
    val cacheDirectory = Paths.get(cacheDirectoryName)
    val cacheFilePath = Paths.get(cacheDirectoryName, cacheFileName)
    val delegate = new HttpStub(uri -> (200, """{ "result": "ok" }"""))
    val oneWayHash = new OneWayHashStub(uriString -> cacheFileName)
    val dataOutput = new DataOutputStreamWrapperStub()
    val cacheFileInfo = new FileInfo(0, null, dataOutput)
    val fileSystem = new FileSystemStub(Set(), Map(cacheFilePath -> cacheFileInfo))
    val rightNow = 0
    val systemClock = new SystemClockStub(rightNow)
    val notifications = new NotificationsStub
    val http: Http = new HttpCache(delegate, oneWayHash, fileSystem, cacheDirectory, expireCache, systemClock, notifications)

    // when
    val (actualStatusCode, actualContent) = http.get(uri)

    // then
    assert(actualStatusCode === 200)
    assert(actualContent === """{ "result": "ok" }""")
    assert(dataOutput.written.size === 2)
    assert(dataOutput.written(0) === DatumInt(200))
    assert(dataOutput.written(1) === DatumUtf("""{ "result": "ok" }"""))
    assert(dataOutput.closed === true)
  }

  class HttpStub(entries: (URI, (Int, String))*) extends Http {
    val map: Map[URI, (Int, String)] = entries.toMap

    override def get(uri: URI): (Int, String) = {
      map(uri)
    }
  }

  class OneWayHashStub(entries: (String, String)*) extends OneWayHash {
    val map: Map[String, String] = entries.toMap

    override def toHexString(source: String): String = map(source)
  }

  class FileSystemStub(existingFiles: Set[Path], fileInfoMap: Map[Path, FileInfo]) extends FileSystemNotImplemented {
    val ensureDirectoriesExistInvocations: ArrayBuffer[Path] = new ArrayBuffer[Path]()

    override def fileExists(path: Path): Boolean = existingFiles.contains(path)

    override def lastModified(path: Path): Long = fileInfoMap(path).lastModified

    override def dataInputFor(path: Path): DataInputStreamWrapper = fileInfoMap(path).dataInput

    override def dataOutputFor(path: Path): DataOutputStreamWrapper = fileInfoMap(path).dataOutput

    override def ensureDirectoriesExist(path: Path): Unit = ensureDirectoriesExistInvocations.append(path)
  }

  class SystemClockStub(now: Long) extends SystemClock {
    override def currentTimeMillis: Long = now
  }

  class NotificationsStub extends NotificationsNotImplemented {
    val httpGetFromCacheInvocations = new ArrayBuffer[HttpGetFromCacheInvocation]

    override def httpGetFromCache(uri: URI, path: Path): Unit = {
      httpGetFromCacheInvocations.append(HttpGetFromCacheInvocation(uri, path))
    }
  }

  class FileInfo(val lastModified: Long, val dataInput: DataInputStreamWrapper, val dataOutput: DataOutputStreamWrapper)

  case class HttpGetFromCacheInvocation(uri: URI, path: Path)

}
