package io.github.rafafrdz.stockingapp.server.matchers

import org.http4s.dsl.io.QueryParamDecoderMatcher

object ItemMatcher {

  object IdQueryParamMatcher extends QueryParamDecoderMatcher[String]("id")
  object NameQueryParamMatcher extends QueryParamDecoderMatcher[String]("name")
}
