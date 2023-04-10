package io.github.rafafrdz.stockingapp.request

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}
trait AutoEntityDerive[T] {
  implicit def EntityDecoder[F[_]: Concurrent](implicit decoder: Decoder[T]): EntityDecoder[F, T] =
    jsonOf[F, T]

  implicit def EntityEncoder[F[_]](implicit encoder: Encoder[T]): EntityEncoder[F, T] =
    jsonEncoderOf[F, T]
}
