package io.skyfii.mandrill.model

case class SubAccountResponse(id: String,
                              name: Option[String] = None,
                              custom_quota: Option[Int] = None,
                              status: String,
                              reputation: Int,
                              created_at: String,
                              first_sent_at: Option[String] = None,
                              sent_weekly: Int,
                              sent_monthly: Int,
                              sent_total: Int)


