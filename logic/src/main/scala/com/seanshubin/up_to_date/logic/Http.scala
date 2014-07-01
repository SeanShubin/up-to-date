package com.seanshubin.up_to_date.logic

trait Http {
  def get(uri: String): (Int, String)
}

object Http {
  def isSuccess(responseCode: Int) = responseCode >= 200 && responseCode <= 399
}
