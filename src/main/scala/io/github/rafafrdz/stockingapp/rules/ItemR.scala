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
  object serializable {
//    implicit val ItemDecoder: Decoder[Item] = deriveDecoder[Item]
//
//    implicit def ItemEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, Item] = jsonOf
//
//    implicit val ItemEncoder: Encoder[Item] = deriveEncoder[Item]
//
//    implicit def ItemEntityEncoder[F[_]]: EntityEncoder[F, Item] = jsonEncoderOf
//
//    implicit val ItemIdDecoder: Decoder[ItemId] = deriveDecoder[ItemId]
//
//    implicit def ItemIdEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, ItemId] = jsonOf
//
//    implicit val ItemIdEncoder: Encoder[ItemId] = deriveEncoder[ItemId]
//
//    implicit def ItemIdEntityEncoder[F[_]]: EntityEncoder[F, ItemId] = jsonEncoderOf
//
//    implicit val ItemNameDecoder: Decoder[ItemName] = deriveDecoder[ItemName]
//
//    implicit def ItemNameEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, ItemName] = jsonOf
//
//    implicit val ItemNameEncoder: Encoder[ItemName] = deriveEncoder[ItemName]
//
//    implicit def ItemNameEntityEncoder[F[_]]: EntityEncoder[F, ItemName] = jsonEncoderOf
//
//    implicit val ItemDescriptionDecoder: Decoder[ItemDescription] = deriveDecoder[ItemDescription]
//
//    implicit def ItemDescriptionEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, ItemDescription] = jsonOf
//
//    implicit val ItemDescriptionEncoder: Encoder[ItemDescription] = deriveEncoder[ItemDescription]
//
//    implicit def ItemDescriptionEntityEncoder[F[_]]: EntityEncoder[F, ItemDescription] = jsonEncoderOf
//
//    implicit val MoneyDecoder: Decoder[Money] = deriveDecoder[Money]
//
//    implicit def MoneyEntityDecoder[F[_] : Concurrent]: EntityDecoder[F, Money] = jsonOf
//
//    implicit val MoneyEncoder: Encoder[Money] = deriveEncoder[Money]
//
//    implicit def MoneyEntityEncoder[F[_]]: EntityEncoder[F, Money] = jsonEncoderOf
//
  }

}
