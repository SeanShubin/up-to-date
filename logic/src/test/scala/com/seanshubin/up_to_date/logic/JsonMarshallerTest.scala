package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

case class SampleForMarshalling(stringSeq: Seq[String],
                                stringSeqSeq: Seq[Seq[String]],
                                optionString: Option[String])

case class UnknownPropertiesTestHelper(bar: Int)

case class NullPropertiesTestHelper(a: String, b: String, c: Option[String], d: Option[String])

class JsonMarshallerTest extends FunSuite {
  val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl
  test("sample to json") {
    val sample = SampleForMarshalling(Seq("aaa"), Seq(Seq("bbb")), Some("ccc"))
    val actual = jsonMarshaller.toJson(sample)
    val expected = normalize( """{ "stringSeq" : [ "aaa" ],  "stringSeqSeq" : [ [ "bbb" ] ],  "optionString" : "ccc"}""")
    assert(actual === expected)
  }
  test("sample from json") {
    val json = """{ "stringSeq" : [ "aaa" ], "stringSeqSeq" : [ [ "bbb" ] ], "optionString" : "ccc"}"""
    val expected = SampleForMarshalling(Seq("aaa"), Seq(Seq("bbb")), Some("ccc"))
    val actual = jsonMarshaller.fromJson(json, classOf[SampleForMarshalling])
    assert(actual === expected)
  }
  test("normalize") {
    val a = normalize( """{"a":[1,2,3],"b":4,"c":{"d":"e"}}""")
    val b = normalize(
      """{
        |  "a" : [1,2,3],
        |  "b" : 4,
        |  "c" :
        |  {
        |    "d" : "e"
        |  }
        |}""".stripMargin)
    assert(a === b)
  }
  test("sensible error message on failure to parse") {
    val jsonMissingClosingBrace = """{ "a" : "b" """
    try {
      jsonMarshaller.fromJson(jsonMissingClosingBrace, classOf[Map[String, String]])
      fail("should have thrown exception")
    } catch {
      case ex: RuntimeException =>
        val expectedMessage =
          """Error while attempting to parse "{ \"a\" : \"b\" ": Unexpected end-of-input: expected close marker for OBJECT (from [Source: { "a" : "b" ; line: 1, column: 0])
            | at [Source: { "a" : "b" ; line: 1, column: 25]""".stripMargin
        val actualMessage = ex.getMessage
        println(expectedMessage)
        println(actualMessage)
        assert(actualMessage === expectedMessage)
    }
  }
  test("ignore unknown properties") {
    val json = """{ "bar": 123, "baz":456 }"""
    val expected = UnknownPropertiesTestHelper(123)
    val actual = jsonMarshaller.fromJson(json, classOf[UnknownPropertiesTestHelper])
    assert(actual === expected)
  }
  test("don't serialize null or empty properties") {
    val theObject = NullPropertiesTestHelper("aaa", null, Some("ccc"), None)
    val expected = jsonMarshaller.normalize( """{ "a": "aaa", "c":"ccc" }""")
    val actual = jsonMarshaller.toJson(theObject)
    assert(actual === expected)
  }
  test("make json pretty") {
    val actual = normalize( """{"a":"b","c":"d"}""")
    val expected =
      """{
        |  "a" : "b",
        |  "c" : "d"
        |}""".stripMargin
    assert(actual === expected)
  }
  test("array from json") {
    val json = """[ 1, 2, 3 ]"""
    val expected = Seq(1, 2, 3)
    val actual = jsonMarshaller.fromJsonArray(json, classOf[Int])
    assert(actual === expected)
  }
  test("merge map into map") {
    assert(merge( """{"a":1, "b":2, "c":3}""", """{"b":3, "c":null, "d":4}""") === normalize( """{"a":1, "b":3, "d":4}"""))
  }

  def merge(a: String, b: String): String = {
    val aObject = jsonMarshaller.fromJson(a, classOf[AnyRef])
    val bObject = jsonMarshaller.fromJson(b, classOf[AnyRef])
    val cObject = jsonMarshaller.merge(aObject, bObject)
    val merged = jsonMarshaller.toJson(cObject)
    merged
  }

  def normalize(s: String) = jsonMarshaller.normalize(s)
}
