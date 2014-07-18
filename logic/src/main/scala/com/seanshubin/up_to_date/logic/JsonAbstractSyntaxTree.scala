package com.seanshubin.up_to_date.logic

import com.fasterxml.jackson.core.{JsonParser, JsonToken}

class JsonAbstractSyntaxTree(parser: JsonParser) {

  import com.seanshubin.up_to_date.logic.JsonAbstractSyntaxTree._

  def parseValue(): JsonValue = {
    parser.nextToken match {
      case JsonToken.START_OBJECT =>
        val members = parseObjectMembers(Seq())
        JsonObject(members)
      case JsonToken.START_ARRAY =>
        val values = parseArrayValues(Seq())
        JsonArray(values)
      case JsonToken.VALUE_STRING => extractString()
      case JsonToken.VALUE_NUMBER_INT => extractWholeNumber()
      case JsonToken.VALUE_NUMBER_FLOAT => extractFloatNumber()
      case JsonToken.VALUE_TRUE => extractTrue()
      case JsonToken.VALUE_FALSE => extractFalse()
      case JsonToken.VALUE_NULL => extractNull()
      case x => unexpected(x)
    }
  }

  def parseObjectMembers(soFar: Seq[JsonPair]): Seq[JsonPair] = {
    parser.nextToken match {
      case JsonToken.FIELD_NAME =>
        val fieldName = extractFieldName()
        parseObjectMembersAfterFieldName(soFar, fieldName)
      case JsonToken.END_OBJECT => soFar
      case x => unexpected(x)
    }
  }

  def parseObjectMembersAfterFieldName(soFar: Seq[JsonPair], fieldName: String): Seq[JsonPair] = {
    val value = parseValue()
    val pair = JsonPair(fieldName, value)
    parseObjectMembers(soFar :+ pair)
  }

  def parseArrayValues(soFar: Seq[JsonValue]): Seq[JsonValue] = {
    parser.nextToken match {
      case JsonToken.START_OBJECT => parseArrayValues(soFar :+ JsonObject(parseObjectMembers(Seq())))
      case JsonToken.START_ARRAY => parseArrayValues(soFar :+ JsonArray(parseArrayValues(Seq())))
      case JsonToken.VALUE_STRING => parseArrayValues(soFar :+ extractString())
      case JsonToken.VALUE_NUMBER_INT => parseArrayValues(soFar :+ extractWholeNumber())
      case JsonToken.VALUE_NUMBER_FLOAT => parseArrayValues(soFar :+ extractFloatNumber())
      case JsonToken.VALUE_TRUE => parseArrayValues(soFar :+ extractTrue())
      case JsonToken.VALUE_FALSE => parseArrayValues(soFar :+ extractFalse())
      case JsonToken.VALUE_NULL => parseArrayValues(soFar :+ extractNull())
      case JsonToken.END_ARRAY => soFar
      case x => unexpected(x)
    }
  }

  def extractString() = JsonString(parser.getValueAsString)

  def extractWholeNumber() = JsonWholeNumber(parser.getValueAsLong)

  def extractFloatNumber() = JsonFloatNumber(parser.getValueAsDouble)

  def extractTrue() = JsonBoolean(value = true)

  def extractFalse() = JsonBoolean(value = false)

  def extractNull() = JsonNull

  def extractFieldName() = parser.getCurrentName

  def unexpected(x: JsonToken): Nothing = {
    val tokenEnum = JsonTokenEnum.fromToken(x)
    val tokenName = tokenEnum.name
    val message = s"json token $tokenName not expected here"
    throw new RuntimeException(message)
  }
}

object JsonAbstractSyntaxTree {

  trait JsonValue {
    def asString: String = throw new UnsupportedOperationException(s"Cannot convert ${this.getClass.getSimpleName} to a String")

    def asLong: Long = throw new UnsupportedOperationException(s"Cannot convert ${this.getClass.getSimpleName} to a Long")

    def asDouble: Double = throw new UnsupportedOperationException(s"Cannot convert ${this.getClass.getSimpleName} to a Double")

    def asBoolean: Boolean = throw new UnsupportedOperationException(s"Cannot convert ${this.getClass.getSimpleName} to a Boolean")

    def asSeq: Seq[JsonValue] = throw new UnsupportedOperationException(s"Cannot convert ${this.getClass.getSimpleName} to an Seq")
  }

  case class JsonString(value: String) extends JsonValue {
    override def asString: String = value
  }

  case class JsonWholeNumber(value: Long) extends JsonValue {
    override def asLong: Long = value
  }

  case class JsonFloatNumber(value: Double) extends JsonValue {
    override def asDouble: Double = value
  }

  case class JsonObject(members: Seq[JsonPair]) extends JsonValue

  case class JsonArray(elements: Seq[JsonValue]) extends JsonValue {
    override def asSeq: Seq[JsonValue] = elements
  }

  case class JsonBoolean(value: Boolean) extends JsonValue {
    override def asBoolean: Boolean = value
  }

  case object JsonNull extends JsonValue

  case class JsonPair(name: String, value: JsonValue)

}
