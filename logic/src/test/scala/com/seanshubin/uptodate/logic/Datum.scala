package com.seanshubin.uptodate.logic

sealed trait Datum

object Datum {
  case class DatumInt(value: Int) extends Datum

  case class DatumUtf(value: String) extends Datum
}
