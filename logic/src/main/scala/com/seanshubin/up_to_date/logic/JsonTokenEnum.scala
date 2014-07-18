package com.seanshubin.up_to_date.logic

import com.fasterxml.jackson.core.JsonToken

import scala.collection.mutable.ArrayBuffer

sealed abstract case class JsonTokenEnum(name: String, javaEnum: JsonToken) {
  JsonTokenEnum.valuesBuffer += this

  def matchesToken(t: JsonToken) = javaEnum == t
}

object JsonTokenEnum {
  private val valuesBuffer = new ArrayBuffer[JsonTokenEnum]
  lazy val values = valuesBuffer.toSeq
  val StartObject = new JsonTokenEnum("START_OBJECT", JsonToken.START_OBJECT) {}
  val EndObject = new JsonTokenEnum("END_OBJECT", JsonToken.END_OBJECT) {}
  val StartArray = new JsonTokenEnum("START_ARRAY", JsonToken.START_ARRAY) {}
  val EndArray = new JsonTokenEnum("END_ARRAY", JsonToken.END_ARRAY) {}
  val FieldName = new JsonTokenEnum("FIELD_NAME", JsonToken.FIELD_NAME) {}
  val ValueString = new JsonTokenEnum("VALUE_STRING", JsonToken.VALUE_STRING) {}
  val ValueNumberInt = new JsonTokenEnum("VALUE_NUMBER_INT", JsonToken.VALUE_NUMBER_INT) {}
  val ValueNumberFloat = new JsonTokenEnum("VALUE_NUMBER_FLOAT", JsonToken.VALUE_NUMBER_FLOAT) {}
  val ValueTrue = new JsonTokenEnum("VALUE_TRUE", JsonToken.VALUE_TRUE) {}
  val ValueFalse = new JsonTokenEnum("VALUE_FALSE", JsonToken.VALUE_FALSE) {}
  val ValueNull = new JsonTokenEnum("VALUE_NULL", JsonToken.VALUE_NULL) {}
  val ValueEmbeddedObject = new JsonTokenEnum("VALUE_EMBEDDED_OBJECT", JsonToken.VALUE_EMBEDDED_OBJECT) {}
  val NotAvailable = new JsonTokenEnum("NOT_AVAILABLE", JsonToken.NOT_AVAILABLE) {}

  def fromToken(target: JsonToken) = {
    val maybeValue = values.find(value => value.matchesToken(target))
    maybeValue match {
      case Some(value) => value
      case None =>
        val validValues = values.map(_.javaEnum).mkString("(", ", ", ")")
        val jsonTokenClass = classOf[JsonToken].getClass.getName
        throw new RuntimeException(
          s"'$target' does not match a valid $jsonTokenClass, valid values are $validValues")
    }
  }
}
