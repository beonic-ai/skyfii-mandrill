package io.skyfii.mandrill.model

case class WebHookEvent(event: Option[String],
                        msg: Option[WebHookMessage],
                        reject: Option[WebHookReject],
                        `type`: Option[String],
                        action: Option[String],
                        ts: Int)
