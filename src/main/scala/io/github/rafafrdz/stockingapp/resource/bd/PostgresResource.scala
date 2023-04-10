package io.github.rafafrdz.stockingapp.resource.bd

import cats.effect.kernel.Async
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

/** An object which has Postgres Connection instances */
object PostgresResource {

  def localhost[F[_]: Async]: Aux[F, Unit] =
    Transactor.fromDriverManager(
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:stockingapp",
      user = "admin",
      pass = "1234",
    )
}
