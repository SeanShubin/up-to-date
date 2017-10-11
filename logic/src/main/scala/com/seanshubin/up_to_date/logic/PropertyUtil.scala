package com.seanshubin.up_to_date.logic

object PropertyUtil {
  def combine(maps: Map[String, String]*): CombineResult = {
    val initialCombineResult = CombineResult(Map(), Map())
    maps.foldLeft(initialCombineResult)(combineMap)
  }

  def expand(target: String, properties: Map[String, String]): String = {
    def expandProperty(soFar: String, propertyKey: String): String = {
      val toReplace = "${" + propertyKey + "}"
      val replaceWith = properties(propertyKey)
      soFar.replace(toReplace, replaceWith)
    }

    if (target.contains("$")) {
      properties.keys.foldLeft(target)(expandProperty)
    } else {
      target
    }
  }

  private def combineMap(combineResult: CombineResult, map: Map[String, String]): CombineResult = {
    map.foldLeft(combineResult)(combineEntry)
  }

  private def combineEntry(combineResult: CombineResult, entry: (String, String)): CombineResult = {
    val (key, value) = entry
    val newCombineResult = combineResult.merged.get(key) match {
      case Some(existingValue) => if (existingValue == value) {
        combineResult
      } else {
        val newConflict = combineResult.conflict.updated(key, combineResult.conflict.getOrElse(key, Set()) + value + existingValue)
        combineResult.copy(conflict = newConflict)
      }
      case None =>
        combineResult.copy(merged = combineResult.merged + entry)
    }
    newCombineResult
  }

  case class CombineResult(merged: Map[String, String], conflict: Map[String, Set[String]])

}
