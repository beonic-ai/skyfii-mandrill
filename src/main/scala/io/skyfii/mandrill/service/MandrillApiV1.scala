package io.skyfii.mandrill.service

import io.skyfii.mandrill.model
import io.skyfii.mandrill.model._
import io.skyfii.mandrill.support.MandrillCodecs._

import argonaut.Argonaut._
import argonaut._
import com.ning.http.client.Response
import dispatch.Defaults._
import dispatch._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.concurrent.Future

class MandrillApiV1(apiKey: String, apiUrlPrefix: String = "https://mandrillapp.com/api/1.0") {

  type AsyncResponse[T] = Future[Either[Error, T]]

  val mandrillUtcDateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  // For our purposes, the  Mandrill API assumes everything is a) over JSON and b) a POST request
  // See https://mandrillapp.com/api/docs/index.JSON.html
  val api = url(apiUrlPrefix).setContentType("application/json", "UTF-8")

  // The service categories
  val users = api / "users"
  val messages = api / "messages"
  val subAccounts = api / "subaccounts"

  /**
   * Ping mandrill API to test an API key
   */
  def ping: AsyncResponse[String] = {
    simpleRequester(users / "ping.json" << entityAsJson(Key(apiKey)))
  }

  /**
   * Get user info for API key
   */
  def usersInfo: AsyncResponse[UserInfo] = {
    postJsonEntity[Key, UserInfo](users / "info.json", Key(apiKey))
  }

  /**
   * Send a message!
   */
  def messagesSend(message: Message,
                   sendAsynchronously: Boolean,
                   ipPool: Option[String] = None,
                   sendAt: Option[DateTime] = None): AsyncResponse[Vector[SendResponse]] = {
    val sendRequest = SendRequest(apiKey,
      message,
      sendAsynchronously,
      ipPool,
      sendAt.map(mandrillUtcDateTime.print(_)))

    postJsonEntity[SendRequest, Vector[SendResponse]](messages / "send.json", sendRequest)
  }

  /**
   * Add a new sub account to the main account
   */
  def subAccountAdd(id: String, name: Option[String] = None, notes: Option[String] = None, quota: Option[Int] = None): AsyncResponse[SubAccountResponse] = {
    val subAccountAddRequest = SubAccountRequest(key = apiKey,
      id = id,
      name = name,
      notes = notes,
      custom_quota = quota)

    postJsonEntity[SubAccountRequest, SubAccountResponse](subAccounts / "add.json", subAccountAddRequest)
  }

  /**
   * Pause the sub account so it cannot be used to send emails anymore
   */
  def subAccountPause(id: String): AsyncResponse[SubAccountResponse] = {
    val subAccountPauseRequest = SubAccountRequest(key = apiKey, id = id)

    postJsonEntity[SubAccountRequest, SubAccountResponse](subAccounts / "pause.json", subAccountPauseRequest)
  }

  /**
   * Resume the sub account so it can be used to send emails again
   */
  def subAccountResume(id: String): AsyncResponse[SubAccountResponse] = {
    val subAccountResumeRequest = SubAccountRequest(key = apiKey, id = id)

    postJsonEntity[SubAccountRequest, SubAccountResponse](subAccounts / "resume.json", subAccountResumeRequest)
  }

  // Error for anything not covered by Mandrill's API
  private def genericError(message: String): model.Error =
    Error("error", -1, "Skyfii_Generic_Error_Key", message)

  // Map any errors to our Error type
  private def mapError(rawResponse: Future[Either[Throwable, Response]]): AsyncResponse[String] = {
    rawResponse map {
      case Left(error) =>
        Left(Error("error", -1, "Skyfii_Http_Error_key", error.getMessage))
      case Right(response) =>
        if (response.getStatusCode >= 400) {
          Left(response.getResponseBody.decodeEither[Error].valueOr(genericError))
        } else {
          Right(response.getResponseBody)
        }
    }
  }

  // Default spacing for json entities
  private def entityAsJson[E](entity: E)(implicit e: EncodeJson[E]): String = {
    entity.asJson.spaces2
  }

  // Simple request-response with entity-independent error handling
  private def simpleRequester(service: Req): AsyncResponse[String] = {
    mapError(Http(service).either)
  }

  // Decode an entity from a Json response else return an error
  private def responseEntity[E](response: Either[Error, String])
                               (implicit d: DecodeJson[E]): Either[Error, E] = {
    response.right.flatMap {
      _.decodeEither[E].leftMap(genericError).toEither
    }
  }

  // Post a Json encoded request entity and extract the Json encoded response entity
  private def postJsonEntity[E1, E2](service: Req, entity: E1)
                                    (implicit d: DecodeJson[E2], e: EncodeJson[E1]): AsyncResponse[E2] = {
    simpleRequester(service << entityAsJson(entity)) map responseEntity[E2]
  }

}

