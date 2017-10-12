package com.seanshubin.uptodate.logic

object EitherUtil {
  def extractLeft[LeftType, RightType](either: Either[LeftType, RightType]): Option[LeftType] = either match {
    case Left(value) => Some(value)
    case Right(_) => None
  }

  def extractRight[LeftType, RightType](either: Either[LeftType, RightType]): Option[RightType] = either match {
    case Left(_) => None
    case Right(value) => Some(value)
  }

  def firstRight[ElementType, LeftType, RightType](seq: Seq[ElementType],
                                                   f: ElementType => Either[LeftType, RightType]): Either[LeftType, RightType] = {
    val result = f(seq.head)
    result match {
      case Right(value) => result
      case _ =>
        if (seq.tail.isEmpty) result
        else firstRight(seq.tail, f)
    }
  }
}
