package io.skyfii.mandrill.model

case class SubAccountRequest(key: String,
                             id: String,
                             name: Option[String] = None,
                             notes: Option[String] = None,
                             custom_quota: Option[Int] = None)
