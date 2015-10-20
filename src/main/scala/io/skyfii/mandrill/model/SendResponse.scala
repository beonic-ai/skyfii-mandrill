package io.skyfii.mandrill.model

case class SendResponse(email: String,
                        status: String,
                        reject_reason: Option[String],
                        _id: String)
