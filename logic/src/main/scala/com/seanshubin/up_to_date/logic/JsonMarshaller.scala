package com.seanshubin.up_to_date.logic

trait JsonMarshaller {
  def fromJson[T](jsonString: String, mappedType: Class[T]): T
}
