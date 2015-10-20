package io.skyfii.mandrill.model

case class SendRequest(key: String,
                       message: Message,
                       async: Boolean,
                       ip_pool: Option[String] = None,
                       send_at: Option[String] = None)
