package com.seanshubin.up_to_date.logic

import org.scalatest.FunSuite

class DynamicJsonTest extends FunSuite {
  val jsonMarshaller: JsonMarshaller = new JsonMarshallerImpl
  test("dynamic json") {
    val sampleJson =
      """{
        |  "a": "b",
        |  "c": {
        |    "d": [
        |      "e",
        |      ["f", "g"],
        |      {
        |        "h":"i"
        |      }
        |    ]
        |  }
        |}
      """.stripMargin
    val dynamicJson = jsonMarshaller.toDynamicJsonObject(sampleJson)
    assert(DynamicJson.unwrap(dynamicJson.a).asString === "b")
    assert(DynamicJson.unwrap(dynamicJson.c.d(0)).asString === "e")
    assert(DynamicJson.unwrap(dynamicJson.c.d(1)(0)).asString === "f")
    assert(DynamicJson.unwrap(dynamicJson.c.d(1)(1)).asString === "g")
    assert(DynamicJson.unwrap(dynamicJson.c.d(2).h).asString === "i")
  }
}
