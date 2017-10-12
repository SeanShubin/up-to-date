package com.seanshubin.uptodate.logic

import java.net.URI

trait Http {
  def get(uri: URI): (Int, String)
}

object Http {
  def isSuccess(responseCode: Int) = responseCode >= 200 && responseCode <= 399
}
