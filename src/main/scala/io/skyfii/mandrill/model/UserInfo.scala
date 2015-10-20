package io.skyfii.mandrill.model

case class UserInfo(username: String,
                    created_at: String,
                    public_id: String,
                    reputation: Int,
                    hourly_quota: Int,
                    backlog: Int)
