package com.seanshubin.up_to_date.logic

import java.nio.file.{Path, Paths}

class HttpCache(delegate: Http,
                oneWayHash: OneWayHash,
                fileSystem: FileSystem,
                cacheDirectory: Path,
                expireCache: Long,
                systemClock: SystemClock,
                notifications: Notifications) extends Http {
  override def get(uri: String): (Int, String) = {
    val fileName = oneWayHash.toHexString(uri)
    val filePath = cacheDirectory.resolve(Paths.get(fileName))
    if (fileSystem.fileExists(filePath)) {
      val lastModified = fileSystem.lastModifiedSeconds(filePath)
      val now = systemClock.currentTimeSeconds
      if (now - lastModified > expireCache) {
        val (statusCode, content) = delegate.get(uri)
        val dataOutput = fileSystem.dataOutputFor(filePath)
        dataOutput.writeInt(statusCode)
        dataOutput.writeUTF(content)
        dataOutput.close()
        (statusCode, content)
      } else {
        notifications.httpGetFromCache(uri, filePath)
        val dataInput = fileSystem.dataInputFor(filePath)
        val statusCode = dataInput.readInt()
        val content = dataInput.readUTF()
        dataInput.close()
        (statusCode, content)
      }
    } else {
      val (statusCode, content) = delegate.get(uri)
      fileSystem.ensureDirectoriesExist(cacheDirectory)
      val dataOutput = fileSystem.dataOutputFor(filePath)
      dataOutput.writeInt(statusCode)
      dataOutput.writeUTF(content)
      dataOutput.close()
      (statusCode, content)
    }
  }
}
