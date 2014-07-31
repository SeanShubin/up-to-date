package com.seanshubin.up_to_date.logic

object StringUtil {
  def escape(target: String) = {
    target.flatMap {
      case '\n' => "\\n"
      case '\b' => "\\b"
      case '\t' => "\\t"
      case '\f' => "\\f"
      case '\r' => "\\r"
      case '\"' => "\\\""
      case '\'' => "\\\'"
      case '\\' => "\\\\"
      case x => x.toString
    }
  }

  def doubleQuote(target: String) = s""""${escape(target)}""""

  def getNewlineSeparator(target: String, default: String): String = {
    val histogram: Map[String, Int] = "\r\n|\r|\n".r.findAllIn(target).foldLeft(Map[String, Int]())(addToHistogram)
    if (histogram.values.sum == 0) {
      default
    } else if (histogram.values.sum == histogram.values.max) {
      histogram.filter(nonZeroValue).keys.head
    } else {
      throw new RuntimeException("Inconsistent newline separator")
    }
  }

  private def nonZeroValue(entry: (String, Int)): Boolean = {
    val (_, value) = entry
    value != 0
  }

  private def addToHistogram[T](histogram: Map[T, Int], value: T): Map[T, Int] = {
    histogram.updated(value, histogram.getOrElse(value, 0) + 1)
  }
}
