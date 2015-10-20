package io.skyfii.mandrill.service

import java.util.UUID

import io.skyfii.mandrill.model.{Message, Recipient, ToType}
import org.joda.time.DateTime
import org.scalatest._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class MandrillApiV1Test extends FunSpec with ShouldMatchers {

  trait Fixtures {
    val time = new DateTime(2015, 4, 1, 0, 0)
    val testKey = "add-your-key-here"
    val api = new MandrillApiV1(testKey)

    def wait[T](f: Future[T]): T = Await.result(f, 30 seconds)
  }

  ignore("run these when Mandrill publishes API changes...") {
    describe("all api services") {

      describe("#ping") {
        it("pings and pongs") {
          new Fixtures {
            val response = wait(api.ping)
            response should be(Right("\"PONG!\""))
          }
        }
        it("fails with bad API key") {
          new Fixtures {
            val badApi = new MandrillApiV1("badKey")
            val response = wait(badApi.ping)
            response.isLeft shouldBe true
          }
        }
      }

      describe("#usersInfo") {
        it("retrieves and decodes user info") {
          new Fixtures {
            val response = wait(api.usersInfo)
            response.isRight shouldBe true
          }
        }
      }

      describe("#messagesSend") {
        it("sends nothing for bad arguments") {
          new Fixtures {
            // Mandrill's API for test users might be dumbed down -it allows obviously fake
            // recipient addresses and sending to 0 recipients. We ignore those cases.

            val message = new Message("<p>test html content</p>", None, "test", "bad from address",
              None, Vector(new Recipient("user1@skyfii.com", None, ToType.cc)))
            val sendResponse = wait(api.messagesSend(message, sendAsynchronously = false, None, None))

            sendResponse.isLeft shouldBe true
            sendResponse.left.get.name === "ValidationError"
          }
        }

        it("sends a message and decodes the response") {
          new Fixtures {
            val recipients = Vector(new Recipient("user1@skyfii.com", Some("daniel"), ToType.to))
            val message = new Message("<p>test html content</p>", None, "test", "noreply@skyfii.com", None, recipients)
            val sendResponse = wait(api.messagesSend(message, sendAsynchronously = false, None, None))
            val recipientResponses = sendResponse.right.get
            recipientResponses.length === 1
            recipientResponses.head.email === "user1@skyfii.com"
            recipientResponses.head.status === "sent"
          }
        }

        it("sends many messages and decodes responses") {
          new Fixtures {
            val emails = (1 to 50) map (n => s"user${n}@skyfii.com") toSet
            val recipients = emails map (email => new Recipient(email, None, ToType.to)) toVector

            val message = new Message("<p>test html content</p>", None, "test many", "noreply@skyfii.com", None, recipients)
            val sendResponse = wait(api.messagesSend(message, sendAsynchronously = true, None, None))
            val recipientResponses = sendResponse.right.get
            recipientResponses.length === 50
            (recipientResponses map (_.email) toSet) === emails

            every(recipientResponses map (_.status)) shouldBe "queued"
          }
        }
      }

      describe("#subAccountAdd") {
        it("adds sub account and decodes the response") {
          new Fixtures {
            val id = UUID.randomUUID().toString
            val response = wait(api.subAccountAdd(id, Some("Test")))
            response.isRight shouldBe true
          }
        }
      }

      describe("#subAccountPause") {
        it("pauses sub account and decodes the response") {
          new Fixtures {
            val id = UUID.randomUUID().toString
            val response = wait(api.subAccountAdd(id, Some("Test")))
            response.isRight shouldBe true
            val pauseResponse = wait(api.subAccountPause(id))
            pauseResponse.isRight shouldBe true
          }
        }
      }

      describe("#subAccountResume") {
        it("resumes sub account and decodes the response") {
          new Fixtures {
            val id = UUID.randomUUID().toString
            val response = wait(api.subAccountAdd(id, Some("Test")))
            response.isRight shouldBe true
            val pauseResponse = wait(api.subAccountPause(id))
            pauseResponse.isRight shouldBe true
            val resumeResponse = wait(api.subAccountResume(id))
            resumeResponse.isRight shouldBe true
          }
        }
      }
    }
  }
}
