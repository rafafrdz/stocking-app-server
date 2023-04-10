package io.github.rafafrdz.stockingapp.request.item

import cats.effect.Concurrent
import io.circe._
import io.circe.generic.semiauto._
import org.http4s._
import org.http4s.circe._

case class ItemError(code: Int, mssg: String) extends Throwable
object ItemError {
  implicit val decoder: Decoder[ItemError] = deriveDecoder[ItemError]
  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, ItemError] = jsonOf
  implicit val encoder: Encoder[ItemError] = deriveEncoder[ItemError]
  implicit def entityEncoder[F[_]]: EntityEncoder[F, ItemError] = jsonEncoderOf
}
