package io.github.rafafrdz.stockingapp

import cats.effect.{IO, IOApp}
import io.github.rafafrdz.stockingapp.server.ItemServer

object Main extends IOApp.Simple {
  val run: IO[Unit] = ItemServer.run[IO]
}
