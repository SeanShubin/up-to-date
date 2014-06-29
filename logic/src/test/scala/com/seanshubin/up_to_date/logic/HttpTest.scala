package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class HttpTest extends FunSuite {
  test("is success") {
    assert(Http.isSuccess(200) === true)
    assert(Http.isSuccess(201) === true)
    assert(Http.isSuccess(299) === true)
    assert(Http.isSuccess(300) === true)
    assert(Http.isSuccess(301) === true)
    assert(Http.isSuccess(399) === true)
    assert(Http.isSuccess(400) === false)
    assert(Http.isSuccess(404) === false)
    assert(Http.isSuccess(499) === false)
    assert(Http.isSuccess(500) === false)
    assert(Http.isSuccess(501) === false)
    assert(Http.isSuccess(599) === false)
  }
}
