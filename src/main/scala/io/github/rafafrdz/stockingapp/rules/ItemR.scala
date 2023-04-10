package io.github.rafafrdz.stockingapp.rules

import cats.implicits._
import cats.{Applicative, Id}
import io.github.rafafrdz.stockingapp.model.Item
import io.github.rafafrdz.stockingapp.model.Item._
import io.github.rafafrdz.stockingapp.resource.dao.ItemDao

import java.util.UUID

object ItemR {

  def make[F[_]: Applicative](
      uuid: String,
      name: String,
      description: String,
      price: Double,
  ): F[Item] =
    (uuid.pure[F], name.pure[F], description.pure[F], price.pure[F]).mapN((u, n, d, p) =>
      Item(toItemId(u), toItemName(n), toItemDescription(d), toMoney(p))
    )

  def create[F[_]: Applicative](name: String, description: String, price: Double): F[Item] =
    (UUID.randomUUID().pure[F], name.pure[F], description.pure[F], price.pure[F]).mapN(
      (u, n, d, p) => Item(ItemId(u), toItemName(n), toItemDescription(d), toMoney(p))
    )

  def create(name: String, description: String, price: Double): Item =
    create[Id](name, description, price)

  def from(itemDao: ItemDao): Item =
    make[Id](
      itemDao.uuid,
      itemDao.name,
      itemDao.description,
      itemDao.price,
    )

  def toItemId(uuid: String): ItemId = ItemId(UUID.fromString(uuid))

  def toItemName(name: String): ItemName = ItemName(name)

  def toItemDescription(name: String): ItemDescription = ItemDescription(name)

  def toMoney(price: Double): Money = Money(price)

}
