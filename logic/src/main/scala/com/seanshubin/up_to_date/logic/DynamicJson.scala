package com.seanshubin.up_to_date.logic

import com.seanshubin.up_to_date.logic.JsonAbstractSyntaxTree.{JsonArray, JsonObject, JsonValue}

import scala.language.dynamics

case class DynamicJson(doNotUseThisAsAJsonFieldName: JsonValue) extends Dynamic {
  def selectDynamic(methodName: String): DynamicJson = {
    val result = doNotUseThisAsAJsonFieldName match {
      case JsonObject(members) =>
        val matchingMembers = members.filter(jsonPair => jsonPair.name == methodName)
        if (matchingMembers.size == 0) throw new RuntimeException(s"json field named '$methodName' not found")
        if (matchingMembers.size > 1) throw new RuntimeException(s"more than one occurrence of json field named '$methodName'")
        DynamicJson(matchingMembers.head.value)
    }
    result
  }

  def applyDynamic(methodName: String)(args: Any*): DynamicJson = {
    val size = args.size
    if (size != 1) throw new RuntimeException(s"exactly 1 parameter expected for method apply, got $size parameters")
    val index = args(0).asInstanceOf[Int]
    val jsonArray =
      if (methodName == "apply") doNotUseThisAsAJsonFieldName
      else selectDynamic(methodName).doNotUseThisAsAJsonFieldName
    val result = jsonArray match {
      case JsonArray(elements) => DynamicJson(elements(index))
    }
    result
  }
}

object DynamicJson {
  def unwrap(dynamicJson: DynamicJson): JsonValue = dynamicJson.doNotUseThisAsAJsonFieldName
}
