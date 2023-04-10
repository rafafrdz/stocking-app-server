package io.github.rafafrdz.stockingapp.request.item

import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import io.github.rafafrdz.stockingapp.request.AutoEntityDerive

@derive(encoder, decoder)
case class EditItem(price: Double)
object EditItem {
  object encodedecoder extends AutoEntityDerive[EditItem]
}
