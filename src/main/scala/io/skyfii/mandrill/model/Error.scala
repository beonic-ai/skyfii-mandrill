package io.skyfii.mandrill.model

// See https://mandrillapp.com/api/docs/users.JSON.html#method=ping
case class Error(status: String,
                 code: Int,
                 name: String,
                 message: String)
