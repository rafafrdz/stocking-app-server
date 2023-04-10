package io.github.rafafrdz.stockingapp.resource.dao

import cats.Applicative
import cats.syntax.all._

case class ItemDao(uuid: String, name: String, description: String, price: Double)

object ItemDao {

  def make[F[_]: Applicative](
      uuid: String,
      name: String,
      description: String,
      price: Double,
  ): F[ItemDao] =
    (uuid.pure[F], name.pure[F], description.pure[F], price.pure[F]).mapN((u, n, d, p) =>
      ItemDao(u, n, d, p)
    )

}
