package io.github.rafafrdz.stockingapp.response

sealed trait ApiCode {
  val code: Int
  val mssg: String
}
object ApiCode {
  case class ApiCodeId(code: Int, mssg: String, uuid: String) extends ApiCode
  case class ApiCodeError private (code: Int, mssg: String) extends ApiCode
  object ApiCodeError {
    def build(error: => Throwable): ApiCodeError = ApiCodeError(-1, error.getMessage)
  }

}
