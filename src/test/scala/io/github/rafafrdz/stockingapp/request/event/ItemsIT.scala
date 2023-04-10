package io.github.rafafrdz.stockingapp.request.event

import cats.effect._
import io.github.rafafrdz.stockingapp.resource.ItemResource
import io.github.rafafrdz.stockingapp.resource.bd.PostgresResource

object ItemsIT extends IOApp {

  def itemsE[F[_]: Async]: Items[F] =
    Items.impl[F](ItemResource.postgres[F](PostgresResource.localhost[F]))

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO.println("")
      items = itemsE[IO]
      code <- items.addItem("pera", "la pera es la pera", 0.14)
      itemNew <- items.findById(code.uuid)
      _ <- IO.println(itemNew)
    } yield ExitCode.Success
}
