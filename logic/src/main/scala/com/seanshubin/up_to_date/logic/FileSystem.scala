package com.seanshubin.up_to_date.logic

trait FileSystem {
  def fileExists(fileName: String): Boolean

  def loadFileIntoString(fileName: String): String
}
