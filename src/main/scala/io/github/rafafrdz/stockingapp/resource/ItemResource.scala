package io.github.rafafrdz.stockingapp.resource

import cats.Id
import cats.data.NonEmptyList
import cats.effect.kernel.Async
import cats.syntax.all._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import doobie.util.transactor.Transactor.Aux
import io.github.rafafrdz.stockingapp.model.Item
import io.github.rafafrdz.stockingapp.model.Item._
import io.github.rafafrdz.stockingapp.resource.dao.ItemDao
import io.github.rafafrdz.stockingapp.rules.ItemR

import java.util.UUID

trait ItemResource[F[_]] {

  def findAll: F[List[Item]]

  def findById(itemId: ItemId): F[Option[Item]]

  def findBy(name: ItemName): F[Option[NonEmptyList[Item]]]

  def addItem(item: Item): F[ItemId]

  def editItem(itemId: ItemId, updated: Item): F[Status]

  def deleteItem(itemId: ItemId): F[Status]
}

object ItemResource {

  /** Implementation should be using Streams instead of Lists. This could avoid future overloading memory issues.
    * Also, implementation should use a Config object in order to create the Aux[F[, Unit] instance.
    */
  def postgres[F[_]: Async](xa: Aux[F, Unit]): ItemResource[F] = new ItemResource[F] {

    override def findAll: F[List[Item]] =
      sql"select * from stock.item"
        .query[Item]
        .to[List]
        .transact(xa)

    override def findById(itemId: ItemId): F[Option[Item]] =
      sql"select * from stock.item where uuid = ${itemId.value.toString}"
        .query[Item]
        .option
        .transact(xa)

    override def findBy(name: ItemName): F[Option[NonEmptyList[Item]]] =
      sql"select * from stock.item where name = ${name.value}"
        .query[Item]
        .to[List]
        .map(_.toNel)
        .transact(xa)

    override def addItem(item: Item): F[ItemId] =
      sql"INSERT INTO stock.item (uuid, name, description, price) VALUES ($item)".update.run
        .transact(xa)
        .map(_ => item.uuid)

    override def editItem(itemId: ItemId, updated: Item): F[Status] =
      sql"""UPDATE stock.item
           SET (name, description, price) = (${updated.name.value}, ${updated.description.value}, ${updated.price.value})
           WHERE uuid = ${itemId.value.toString}""".stripMargin.update.run
        .transact(xa)
        .map(Status.fromCode)

    override def deleteItem(itemId: ItemId): F[Status] =
      sql"DELETE FROM stock.item WHERE uuid = ${itemId.value.toString}".update.run
        .transact(xa)
        .map(Status.fromCode)
  }

  /** Reader and writer for Item case class to/from Postgres
    * Reader is using Item rules and ItemDao smart constructor
    * to get in one way the correct Item. It is important to use both method to validate
    * a correct instance of Item object.
    */
  implicit private val itemReader: Read[Item] = Read[(String, String, String, Double)].map {
    case (u, n, d, p) =>
      val dao: ItemDao = ItemDao.make[Id](u, n, d, p)
      ItemR.from(dao)
  }
  implicit private val itemWriter: Write[Item] =
    Write[(UUID, String, String, Double)].contramap(c =>
      (c.uuid.value, c.name.value, c.description.value, c.price.value)
    )
}
