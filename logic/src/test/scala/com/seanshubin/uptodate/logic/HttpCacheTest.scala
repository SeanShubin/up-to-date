package com.seanshubin.uptodate.logic

import java.net.URI
import java.nio.file.Paths

import org.scalatest.FunSuite
import org.scalatest.easymock.EasyMockSugar

class HttpCacheTest extends FunSuite with EasyMockSugar {
  test("load from filesystem when in cache and not expired") {
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

    val delegate = mock[Http]
    val oneWayHash = mock[OneWayHash]
    val fileSystem = mock[FileSystem]
    val systemClock = mock[SystemClock]
    val notifications = mock[Notifications]
    val dataInputStreamWrapper = mock[DataInputStreamWrapper]

    val http: Http = new HttpCache(delegate, oneWayHash, fileSystem, cacheDirectory, expireCache, systemClock, notifications)

    expecting {
      oneWayHash.toHexString(uriString).andReturn(cacheFileName)
      fileSystem.fileExists(cacheFilePath).andReturn(true)
      fileSystem.lastModified(cacheFilePath).andReturn(oneDayAgo)
      systemClock.currentTimeMillis.andReturn(rightNow)
      notifications.httpGetFromCache(uri, cacheFilePath)
      fileSystem.dataInputFor(cacheFilePath).andReturn(dataInputStreamWrapper)
      dataInputStreamWrapper.readInt().andReturn(200)
      dataInputStreamWrapper.readUTF().andReturn( """{ "result": "ok" }""")
      dataInputStreamWrapper.close()
    }

    whenExecuting(delegate, oneWayHash, fileSystem, systemClock, notifications, dataInputStreamWrapper) {
      val (actualStatusCode, actualContent) = http.get(uri)
      assert(actualStatusCode === 200)
      assert(actualContent === """{ "result": "ok" }""")
    }
  }

  test("load from uri and update cache when in cache and expired") {
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

    val delegate = mock[Http]
    val oneWayHash = mock[OneWayHash]
    val fileSystem = mock[FileSystem]
    val systemClock = mock[SystemClock]
    val notifications = mock[Notifications]
    val dataOutputStreamWrapper = mock[DataOutputStreamWrapper]

    val http: Http = new HttpCache(delegate, oneWayHash, fileSystem, cacheDirectory, expireCache, systemClock, notifications)

    expecting {
      oneWayHash.toHexString(uriString).andReturn(cacheFileName)
      fileSystem.fileExists(cacheFilePath).andReturn(true)
      fileSystem.lastModified(cacheFilePath).andReturn(sixDaysAgo)
      systemClock.currentTimeMillis.andReturn(rightNow)
      delegate.get(uri).andReturn((200, """{ "result": "ok" }"""))
      fileSystem.dataOutputFor(cacheFilePath).andReturn(dataOutputStreamWrapper)
      dataOutputStreamWrapper.writeInt(200)
      dataOutputStreamWrapper.writeUTF( """{ "result": "ok" }""")
      dataOutputStreamWrapper.close()
    }

    whenExecuting(delegate, oneWayHash, fileSystem, systemClock, notifications, dataOutputStreamWrapper) {
      val (actualStatusCode, actualContent) = http.get(uri)
      assert(actualStatusCode === 200)
      assert(actualContent === """{ "result": "ok" }""")
    }
  }

  test("load from uri and update cache when not in cache") {
    val uriString: String = "http://localhost:12345/foo"
    val uri: URI = new URI(uriString)
    val cacheFileName = "hashed-uri"
    val expireCache = DurationFormat.MillisecondsFormat.parse("5 days")
    val cacheDirectoryName = "cache"
    val cacheDirectory = Paths.get(cacheDirectoryName)
    val cacheFilePath = Paths.get(cacheDirectoryName, cacheFileName)

    val delegate = mock[Http]
    val oneWayHash = mock[OneWayHash]
    val fileSystem = mock[FileSystem]
    val systemClock = mock[SystemClock]
    val notifications = mock[Notifications]
    val dataOutputStreamWrapper = mock[DataOutputStreamWrapper]

    val http: Http = new HttpCache(delegate, oneWayHash, fileSystem, cacheDirectory, expireCache, systemClock, notifications)

    expecting {
      oneWayHash.toHexString(uriString).andReturn(cacheFileName)
      fileSystem.fileExists(cacheFilePath).andReturn(false)
      delegate.get(uri).andReturn((200, """{ "result": "ok" }"""))
      fileSystem.ensureDirectoriesExist(cacheDirectory)
      fileSystem.dataOutputFor(cacheFilePath).andReturn(dataOutputStreamWrapper)
      dataOutputStreamWrapper.writeInt(200)
      dataOutputStreamWrapper.writeUTF( """{ "result": "ok" }""")
      dataOutputStreamWrapper.close()
    }

    whenExecuting(delegate, oneWayHash, fileSystem, systemClock, notifications, dataOutputStreamWrapper) {
      val (actualStatusCode, actualContent) = http.get(uri)
      assert(actualStatusCode === 200)
      assert(actualContent === """{ "result": "ok" }""")
    }
  }
}
