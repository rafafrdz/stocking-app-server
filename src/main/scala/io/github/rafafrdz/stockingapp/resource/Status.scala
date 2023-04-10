package io.github.rafafrdz.stockingapp.resource

sealed trait Status {
  val code: Int
}
object Status {
  case object Okay extends Status {
    override val code: Int = 1
  }
  case object Unreachable extends Status {
    override val code: Int = -1
  }

  def fromCode(code: Int): Status =
    code match {
      case Okay.code => Okay
      case _ => Unreachable
    }
}
