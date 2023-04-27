package io.github.rafafrdz.stockingapp.server

import cats.effect.Async
import cats.implicits.toSemigroupKOps
import com.comcast.ip4s._
import io.github.rafafrdz.stockingapp.request.event.Items
import io.github.rafafrdz.stockingapp.resource.ItemResource
import io.github.rafafrdz.stockingapp.resource.bd.PostgresResource
import io.github.rafafrdz.stockingapp.server.routes.{HtmlRoutes, ItemRoutes}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object ItemServer {

  def run[F[_]: Async]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      itemAlg = Items.impl[F](ItemResource.postgres[F](PostgresResource.localhost[F]))

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract segments not checked
      // in the underlying routes.
      httpApp = (
        ItemRoutes.itemRoutes[F](itemAlg) <+>
          HtmlRoutes.impl[F](itemAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

      _ <-
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
