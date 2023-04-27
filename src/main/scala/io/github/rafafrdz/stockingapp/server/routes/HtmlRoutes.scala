package io.github.rafafrdz.stockingapp.server.routes

import cats.effect.kernel.Concurrent
import cats.implicits._
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.github.rafafrdz.stockingapp.model.Item
import io.github.rafafrdz.stockingapp.request.event.Items
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.{HttpRoutes, MediaType}

object HtmlRoutes {
  def impl[F[_]: Concurrent](I: Items[F]): HttpRoutes[F] = {
    val ItemsPath: String = "html"
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / ItemsPath / "tables" =>
        for {
          items <- I.findAll
          body <- html(tableHtml(items)).pure[F]
          resp <- Ok(body, `Content-Type`(MediaType.text.html))
        } yield resp
      case GET -> Root / ItemsPath / "hello" / name =>
        for {
          body <- html(myNameIsHtml(name)).pure[F]
          resp <- Ok(body, `Content-Type`(MediaType.text.html))
        } yield resp

    }
  }

  def html(body: String): String =
    s"<!DOCTYPE html><html><body><h1>Hello World!</h1>$body</body></html>".stripMargin

  def myNameIsHtml(name: String): String =
    s"<p>My name is $name</p>"

  def tableHtml(items: List[Item]): String = {
    val header: String = tr {
      List("uuid", "name", "price", "description").map(th).mkString("")
    }
    s"<table>$header${items.map(rowTableHtml).mkString("")}</table>"
  }

  def rowTableHtml(item: Item): String =
    tr {
      List(
        item.uuid.value.toString,
        item.name.value,
        item.price.value.toString,
        item.description.value,
      ).map(td).mkString("")
    }

  def th(value: String): String = s"<th>$value</th>"
  def tr(value: String): String = s"<tr>$value</tr>"
  def td(value: String): String = s"<td>$value</td>"
}
