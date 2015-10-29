package io.skyfii.mandrill.model

case class WebHookMessage(ts: Int,
                          subject: String,
                          email: String,
                          sender: String,
                          tags: List[String],
                          state: String,
                          metadata: Option[Map[String, String]],
                          subaccount: Option[String])
