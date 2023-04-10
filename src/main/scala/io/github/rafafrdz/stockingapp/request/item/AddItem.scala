package io.github.rafafrdz.stockingapp.request.item

import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import io.github.rafafrdz.stockingapp.request.AutoEntityDerive

@derive(encoder, decoder)
case class AddItem(name: String, description: String, price: Double)
object AddItem {
  object encodedecoder extends AutoEntityDerive[AddItem]
}
