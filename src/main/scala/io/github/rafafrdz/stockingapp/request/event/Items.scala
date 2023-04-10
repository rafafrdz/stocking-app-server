package io.github.rafafrdz.stockingapp.request.event

import cats.MonadThrow
import cats.data.NonEmptyList
import cats.implicits._
import io.github.rafafrdz.stockingapp.model.Item
import io.github.rafafrdz.stockingapp.request.item.ItemError
import io.github.rafafrdz.stockingapp.response.ApiCode.ApiCodeId
import io.github.rafafrdz.stockingapp.resource.{ItemResource, Status}
import io.github.rafafrdz.stockingapp.rules.ItemR

trait Items[F[_]] {
  def findAll: F[List[Item]]
  def findById(id: String): F[Item]
  def findBy(name: String): F[NonEmptyList[Item]]
  def addItem(name: String, description: String, price: Double): F[ApiCodeId]
  def editItem(id: String, price: Double): F[ApiCodeId]
  def deleteItem(id: String): F[ApiCodeId]

}

object Items {

  def impl[F[_]: MonadThrow](res: ItemResource[F]): Items[F] = new Items[F] {

    override def findAll: F[List[Item]] = res.findAll

    override def findById(id: String): F[Item] =
      for {
        itemId <- ItemR.toItemId(id).pure[F].orRaise(ItemError(-2, s"Id $id is not valid."))
        item <- res.findById(itemId).reject { case None =>
          ItemError(-2, s"Item with id $id was not found.")
        }
      } yield item.get

    override def findBy(name: String): F[NonEmptyList[Item]] =
      for {
        itemName <- ItemR.toItemName(name).pure[F]
        item <- res.findBy(itemName).reject { case None =>
          ItemError(-2, s"Item with name $name was not found.")
        }
      } yield item.get

    override def addItem(name: String, description: String, price: Double): F[ApiCodeId] =
      for {
        item <- ItemR.create[F](name, description, price).orRaise(ItemError(-1, "Malformed Item"))
        _ <- res.addItem(item)
        code <- ApiCodeId(0, s"status: ${Status.Okay}", item.uuid.value.toString).pure[F]
      } yield code

    override def editItem(id: String, price: Double): F[ApiCodeId] =
      for {
        (itemId, itemPrice) <- (ItemR.toItemId(id), ItemR.toMoney(price)).pure[F]
        item <- findById(id)
        status <- res.editItem(itemId, item.copy(price = itemPrice))
        code <- ApiCodeId(status.code, s"status: $status", item.uuid.value.toString).pure[F]
      } yield code

    override def deleteItem(id: String): F[ApiCodeId] =
      for {
        itemId <- ItemR.toItemId(id).pure[F]
        item <- findById(id)
        status <- res.deleteItem(itemId)
        code <- ApiCodeId(status.code, s"status: $status", item.uuid.value.toString).pure[F]
      } yield code
  }
}
