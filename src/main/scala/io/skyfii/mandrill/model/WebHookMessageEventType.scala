package io.skyfii.mandrill.model

object WebHookMessageEventType {
  val Send = "send"
  val Deferral = "deferral"
  val HardBounce = "hard_bounce"
  val SoftBounce = "soft_bounce"
  val Open = "open"
  val Click = "click"
  val Spam = "spam"
  val Unsubscribe = "unsub"
  val Reject = "reject"
}
