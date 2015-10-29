package io.skyfii.mandrill.support

import argonaut._
import Argonaut._

import io.skyfii.mandrill.model.ToType.ToType
import io.skyfii.mandrill.model._

object MandrillCodecs {

  implicit def toTypeEncodeJson: EncodeJson[ToType] = EncodeJson(x => jString(x.toString))

  implicit def toTypeDecodeJson: DecodeJson[ToType] = implicitly[DecodeJson[String]].map(ToType.withName)

  implicit def keyCodecJson: CodecJson[Key] = casecodec1(Key.apply, Key.unapply)("key")

  implicit def mandrillErrorCodecJson: CodecJson[Error] =
    casecodec4(Error.apply, Error.unapply)("status", "code", "name", "message")

  implicit def userInfoCodecJson: CodecJson[UserInfo] =
    casecodec6(UserInfo.apply, UserInfo.unapply)(
      "username",
      "created_at",
      "public_id",
      "reputation",
      "hourly_quota",
      "backlog"
    )

  implicit def recipientCodecJson: CodecJson[Recipient] =
    casecodec3(Recipient.apply, Recipient.unapply)("email", "name", "type")

  implicit def mergeVarCodecJson: CodecJson[MergeVar] =
    casecodec2(MergeVar.apply, MergeVar.unapply)("name", "content")

  implicit def recipientMergeVarsCodecJson: CodecJson[RecipientMergeVars] =
    casecodec2(RecipientMergeVars.apply, RecipientMergeVars.unapply)("rcpt", "vars")

  implicit def recipientMetadataCodecJson: CodecJson[RecipientMetadata] =
    casecodec2(RecipientMetadata.apply, RecipientMetadata.unapply)("rcpt", "values")

  implicit def attachmentCodecJson: CodecJson[Attachment] =
    casecodec3(Attachment.apply, Attachment.unapply)("type", "name", "content")

  implicit def imageCodecJson: CodecJson[Image] =
    casecodec3(Image.apply, Image.unapply)("type", "name", "content")

  implicit def messageEncodeJson: EncodeJson[Message] =
    EncodeJson(m => Json(
      "html" := m.html,
      "text" := m.text,
      "subject" := m.subject,
      "from_email" := m.from_email,
      "fromName" := m.from_name,
      "to" := m.to,
      "headers" := m.headers,
      "important" := m.important,
      "track_opens" := m.track_opens,
      "track_clicks" := m.track_clicks,
      "auto_text" := m.auto_text,
      "auto_html" := m.auto_html,
      "inline_css" := m.inline_css,
      "url_strip_qs" := m.url_strip_qs,
      "preserve_recipients" := m.preserve_recipients,
      "view_content_link" := m.view_content_link,
      "bcc_address" := m.bcc_address,
      "tracking_domain" := m.tracking_domain,
      "signing_domain" := m.signing_domain,
      "return_path_domain" := m.return_path_domain,
      "merge" := m.merge,
      "merge_language" := m.merge_language,
      "global_merge_vars" := m.global_merge_vars,
      "merge_vars" := m.merge_vars,
      "tags" := m.tags,
      "subaccount" := m.subaccount,
      "google_analytics_domains" := m.google_analytics_domains,
      "google_analytics_campaign" := m.google_analytics_campaign,
      "metadata" := m.metadata,
      "recipient_metadata" := m.recipient_metadata,
      "attachments" := m.attachments,
      "images" := m.images))

  implicit def messageDecodeJson: DecodeJson[Message] =
    DecodeJson(m => for {
      html <- (m --\ "html").as[String]
      text <- (m --\ "text").as[Option[String]]
      subject <- (m --\ "subject").as[String]
      from_email <- (m --\ "from_email").as[String]
      from_name <- (m --\ "from_name").as[Option[String]]
      to <- (m --\ "to").as[Vector[Recipient]]
      headers <- (m --\ "headers").as[Map[String, String]]
      important <- (m --\ "important").as[Boolean]
      track_opens <- (m --\ "track_opens").as[Boolean]
      track_clicks <- (m --\ "track_clicks").as[Boolean]
      auto_text <- (m --\ "auto_text").as[Boolean]
      auto_html <- (m --\ "auto_html").as[Boolean]
      inline_css <- (m --\ "inline_css").as[Boolean]
      url_strip_qs <- (m --\ "url_strip_qs").as[Boolean]
      preserve_recipients <- (m --\ "preserve_recipients").as[Boolean]
      view_content_link <- (m --\ "view_content_link").as[Boolean]
      bcc_address <- (m --\ "bcc_address").as[Option[String]]
      tracking_domain <- (m --\ "tracking_domain").as[Option[String]]
      signing_domain <- (m --\ "signing_domain").as[String]
      return_path_domain <- (m --\ "return_path_domain").as[String]
      merge <- (m --\ "merge").as[Boolean]
      merge_language <- (m --\ "merge_language").as[Option[String]]
      global_merge_vars <- (m --\ "global_merge_vars").as[Vector[MergeVar]]
      merge_vars <- (m --\ "merge_vars").as[Vector[RecipientMergeVars]]
      tags <- (m --\ "tags").as[Vector[String]]
      subaccount <- (m --\ "subaccount").as[Option[String]]
      google_analytics_domains <- (m --\ "google_analytics_domains").as[Vector[String]]
      google_analytics_campaign <- (m --\ "google_analytics_campaign").as[Option[String]]
      metadata <- (m --\ "metadata").as[Map[String, String]]
      recipient_metadata <- (m --\ "recipient_metadata").as[Vector[RecipientMetadata]]
      attachments <- (m --\ "attachments").as[Vector[Attachment]]
      images <- (m --\ "images").as[Vector[Image]]
    } yield new Message(html,
        text,
        subject,
        from_email,
        from_name,
        to,
        headers,
        important,
        track_opens,
        track_clicks,
        auto_text,
        auto_html,
        inline_css,
        url_strip_qs,
        preserve_recipients,
        view_content_link,
        bcc_address,
        tracking_domain,
        signing_domain,
        return_path_domain,
        merge,
        merge_language,
        global_merge_vars,
        merge_vars,
        tags,
        subaccount,
        google_analytics_domains,
        google_analytics_campaign,
        metadata,
        recipient_metadata,
        attachments,
        images))

  implicit def sendRequestCodecJson: CodecJson[SendRequest] =
    casecodec5(SendRequest.apply, SendRequest.unapply)(
      "key",
      "message",
      "async",
      "ip_pool",
      "send_at")

  implicit def sendResponseCodecJson: CodecJson[SendResponse] =
    casecodec4(SendResponse.apply, SendResponse.unapply)("email", "status", "reject_reason", "_id")

  implicit def subAccountResponseCodecJson: CodecJson[SubAccountResponse] =
    casecodec10(SubAccountResponse.apply, SubAccountResponse.unapply)(
      "id",
      "name",
      "custom_quota",
      "status",
      "reputation",
      "created_at",
      "first_sent_at",
      "sent_weekly",
      "sent_monthly",
      "sent_total")

  implicit def subAccountRequestCodecJson: CodecJson[SubAccountRequest] =
    casecodec5(SubAccountRequest.apply, SubAccountRequest.unapply)(
      "key",
      "id",
      "name",
      "notes",
      "custom_quota")

  implicit def webHookMessageJson: CodecJson[WebHookMessage] =
    casecodec8(WebHookMessage.apply, WebHookMessage.unapply)(
      "ts", "subject", "email", "sender", "tags", "state", "metadata", "subaccount")

  implicit def webHookRejectJson: CodecJson[WebHookReject] =
    casecodec3(WebHookReject.apply, WebHookReject.unapply)("email", "reason", "detail")

  implicit def webHookEventJson: CodecJson[WebHookEvent] =
    casecodec6(WebHookEvent.apply, WebHookEvent.unapply)("event", "msg", "reject", "type", "action", "ts")
}

