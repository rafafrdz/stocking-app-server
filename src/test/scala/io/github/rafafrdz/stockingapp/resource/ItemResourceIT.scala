package io.github.rafafrdz.stockingapp.resource

import cats.effect._
import cats.syntax.all._
import io.github.rafafrdz.stockingapp.resource.bd.PostgresResource
import io.github.rafafrdz.stockingapp.rules.ItemR

object ItemResourceIT extends IOApp {

  lazy val res: ItemResource[IO] = ItemResource.postgres[IO](PostgresResource.localhost)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      psql <- PostgresResource.localhost[IO].pure[IO]
      res <- ItemResource.postgres(psql).pure[IO]
      _ <- IO.println("Creating items...")
      appleId <- res.addItem(ItemR.create("apple", "It is so delicious!", 0.45))
      wtmId <- res.addItem(ItemR.create("watermelon", "So fresh!", 2.45))
      _ <- IO.println("apple uuid: " + appleId)
      _ <- IO.println("watermelon uuid: " + wtmId)
      _ <- IO.println("Finding all items...")
      items <- res.findAll
      _ <- IO.println("All items: " + items.mkString(", "))
      _ <- IO.println(s"Finding the item with id: ${wtmId.value}")
      wtmOptItem <- res.findById(wtmId)
      wtmItem = wtmOptItem.get
      _ <- IO.println(s"The item with id: ${wtmId.value} is $wtmItem")
      _ <- IO.println(s"Editing the item with id: ${wtmId.value}")
      wtmlNew <- wtmItem.copy(price = ItemR.toMoney(1.49)).pure[IO]
      status <- res.editItem(wtmId, wtmlNew)
      _ <- IO.println(s"It was $status")
      wtmNewOptItem <- res.findById(wtmId)
      wtmNewItem = wtmNewOptItem.get
      _ <- IO.println(s"The item with id: ${wtmId.value} is now $wtmNewItem")
      _ <- IO.println(s"Deleting the item with id: ${appleId.value}")
      status <- res.deleteItem(appleId)
      _ <- IO.println(s"It was $status")
      findingItem <- res.findById(appleId)
      _ <- IO.println(s"Triying to find this item... it was $findingItem")
      _ <- IO.println(s"Cleaning the table...")
      items <- res.findAll
      _ <- items.map(it => res.deleteItem(it.uuid)).sequence
    } yield ExitCode.Success
}
