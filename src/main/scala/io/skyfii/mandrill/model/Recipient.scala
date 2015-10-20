package io.skyfii.mandrill.model

import io.skyfii.mandrill.model.ToType.ToType

case class Recipient(email: String, name: Option[String], `type`: ToType)
