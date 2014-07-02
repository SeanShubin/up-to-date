package com.seanshubin.up_to_date.logic

import java.io.Closeable

object CloseableUtil {
  def closeAfter[ResultType, ClosableType <: Closeable](closable: ClosableType)(block: ClosableType => ResultType): ResultType = {
    val result = try {
      block(closable)
    } finally {
      closable.close()
    }
    result
  }
}
