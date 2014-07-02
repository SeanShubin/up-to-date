package com.seanshubin.up_to_date.logic

import java.io.Reader

class ReaderIterator(reader: Reader) extends Iterator[Char] {
  private var current: Int = reader.read()

  override def hasNext: Boolean = current != -1

  override def next(): Char = {
    val result = current
    current = reader.read()
    result.toChar
  }
}
