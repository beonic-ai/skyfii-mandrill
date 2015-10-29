package io.skyfii.mandrill.model

case class WebHookReject(email: String,
                         reason: Option[String],
                         detail: Option[String])
