package io.skyfii.mandrill.model

object WebHookMessageEventType extends Enumeration {
  type WebHookMessageEventType = Value
  val send, deferral, hard_bounce, soft_bounce, open, click, spam, unsub, reject = Value
}
