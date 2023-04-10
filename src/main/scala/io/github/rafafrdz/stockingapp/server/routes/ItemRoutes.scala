package io.github.rafafrdz.stockingapp.server.routes

import cats.effect.kernel.Concurrent
import cats.implicits._
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.github.rafafrdz.stockingapp.request.event.Items
import io.github.rafafrdz.stockingapp.request.item.AddItem.encodedecoder._
import io.github.rafafrdz.stockingapp.request.item.{AddItem, EditItem}
import io.github.rafafrdz.stockingapp.server.matchers.ItemMatcher._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

object ItemRoutes {

  def itemRoutes[F[_]: Concurrent](I: Items[F]): HttpRoutes[F] = {
    val ItemsPath: String = "items"
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / ItemsPath =>
        for {
          items <- I.findAll
          resp <- Ok(items)
        } yield resp
      case GET -> Root / ItemsPath / "findById" :? IdQueryParamMatcher(id) =>
        for {
          item <- I.findById(id)
          resp <- Ok(item)
        } yield resp

      case GET -> Root / ItemsPath / "findByName" :? NameQueryParamMatcher(name) =>
        for {
          item <- I.findBy(name)
          resp <- Ok(item)
        } yield resp

      case data @ POST -> Root / ItemsPath / "create" =>
        for {
          addItem <- data.as[AddItem]
          item <- I.addItem(addItem.name, addItem.description, addItem.price)
          resp <- Ok(item)
        } yield resp

      case data @ POST -> Root / ItemsPath / "edit" :? IdQueryParamMatcher(id) =>
        for {
          editItem <- data.as[EditItem]
          code <- I.editItem(id, editItem.price)
          resp <- Ok(code)
        } yield resp

      case PUT -> Root / ItemsPath / "delete" :? IdQueryParamMatcher(id) =>
        for {
          code <- I.deleteItem(id)
          resp <- Ok(code)
        } yield resp
    }
  }
}
