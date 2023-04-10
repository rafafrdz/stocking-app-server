package io.github.rafafrdz.stockingapp.model

import derevo.circe.magnolia.encoder
import derevo.derive
import io.estatico.newtype.macros.newtype
import io.github.rafafrdz.stockingapp.model.Item.{ItemDescription, ItemId, ItemName, Money}

import java.util.UUID

@derive(encoder)
case class Item(
    uuid: ItemId,
    name: ItemName,
    description: ItemDescription,
    price: Money,
)

object Item {

  @derive(encoder)
  @newtype
  case class ItemId(value: UUID)

  @derive(encoder)
  @newtype
  case class ItemName(value: String)

  @derive(encoder)
  @newtype
  case class ItemDescription(value: String)

  @derive(encoder)
  @newtype
  case class Money(value: Double)

}
