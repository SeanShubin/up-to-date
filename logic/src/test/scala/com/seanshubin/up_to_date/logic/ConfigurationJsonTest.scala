package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class ConfigurationJsonTest extends FunSuite {

  import com.seanshubin.up_to_date.logic.SampleData._

  test("report directory must be specified") {
    val actual = configurationJsonComplete.copy(reportDirectory = None).validate()
    val expected = Left(Seq("reportDirectory must be specified"))
    assert(expected === actual)
  }

  test("cache directory must be specified") {
    val actual = configurationJsonComplete.copy(cacheDirectory = None).validate()
    val expected = Left(Seq("cacheDirectory must be specified"))
    assert(expected === actual)
  }

  test("cache expire must be specified") {
    val actual = configurationJsonComplete.copy(cacheExpire = None).validate()
    val expected = Left(Seq("cacheExpire must be specified"))
    assert(expected === actual)
  }

  test("must be able to convert expire cache to milliseconds") {
    val actual = configurationJsonComplete.copy(cacheExpire = Some("not milliseconds")).validate()
    val expected = Left(Seq("unable to parse milliseconds for cacheExpire: 'not milliseconds' does not match a valid pattern: \\d+\\s+[a-zA-Z]+(?:\\s+\\d+\\s+[a-zA-Z]+)*"))
    assert(expected === actual)
  }

  test("valid configuration") {
    val actual = configurationJsonComplete.validate()
    val expected = Right(validConfiguration)
    assert(expected === actual)
  }
}