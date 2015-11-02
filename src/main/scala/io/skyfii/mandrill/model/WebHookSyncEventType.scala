package io.skyfii.mandrill.model

object WebHookSyncEventType extends Enumeration {
  type WebHookSyncEventType = Value
  val whitelist, blacklist = Value
}
