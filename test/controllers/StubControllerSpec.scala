/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, ControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import controllers.actions.HeaderValidatorAction
import models.SuccessfulValidation
import service.{ValidationService, Validator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StubControllerSpec extends SpecBase {

  private implicit val cc: ControllerComponents = app.injector.instanceOf[ControllerComponents]
  private val headerValidatorAction: HeaderValidatorAction = app.injector.instanceOf[HeaderValidatorAction]

  private val displayTrustsSchema = "/resources/schemas/display-trusts-3.0.json"
  private val displayValidator = new ValidationService().get(displayTrustsSchema)

  val SUT = new StubController(headerValidatorAction)

  "StubController getTrusts " should {

    "return OK with valid processed payload for 2000000000" in {
      testProcessedEstate("2000000000")
    }

    "return FORBIDDEN with no headers" in {
      val utr = "2000000000"
      val request = FakeRequest("GET", s"/trusts/registration/$utr")

      val result = SUT.getEstate(utr).apply(request)
      status(result) must be(FORBIDDEN)
    }

    "return FORBIDDEN with only a Token header" in {
      val utr = "2000000000"
      val request = FakeRequest("GET", s"/trusts/registration/$utr")
              .withHeaders((TOKEN_HEADER, "Bearer 11"))

      val result = SUT.getEstate(utr).apply(request)
      status(result) must be(FORBIDDEN)
    }

    "return UNAUTHORIZED with no Token header" in {
      val utr = "2000000000"
      val request = FakeRequest("GET", s"/trusts/registration/$utr")
        .withHeaders((ENVIRONMENT_HEADER, "dev"))

      val result = SUT.getEstate(utr).apply(request)
      status(result) must be(UNAUTHORIZED)
    }

    "return FORBIDDEN with no correlation ID" in {
      val utr = "2000000000"
      val request = FakeRequest("GET", s"/trusts/registration/$utr")
              .withHeaders((ENVIRONMENT_HEADER, "dev"),
              (TOKEN_HEADER, "Bearer 11"))

      val result = SUT.getEstate(utr).apply(request)
      status(result) must be(FORBIDDEN)
    }


    "return OK with valid processed payload for 2000000001" in {
      testProcessedEstate("2000000001")
    }

    "return OK with valid processed payload for 2000000002" in {
      testProcessedEstate("2000000002")
    }

    "return OK with valid processed payload for 2000000003" in {
      testProcessedEstate("2000000003")
    }

    "return OK with valid processed payload for 2000000004" in {
      testProcessedEstate("2000000004")
    }

    "return OK with valid processed payload for 2000000005" in {
      testProcessedEstate("2000000005")
    }

    "return OK with valid processed payload for 4000000000" in {
      testProcessedEstate("4000000000")
    }

    "return OK with valid processed payload for 4000000001" in {
      testProcessedEstate("4000000001")
    }

    "return OK with valid processed payload for 4000000002" in {
      testProcessedEstate("4000000002")
    }

    "return OK with valid processed payload for 4000000003" in {
      testProcessedEstate("4000000003")
    }

    "return OK with valid processed payload for 4000000004" in {
      testProcessedEstate("4000000004")
    }

    "return OK with valid processed payload for 4000000005" in {
      testProcessedEstate("4000000005")
    }

    "return OK with valid processed payload for 4000000006" in {
      testProcessedEstate("4000000006")
    }

    "return OK with valid processed payload for 4000000007" in {
      testProcessedEstate("4000000007")
    }

    "return OK with valid processed payload for 4000000008" in {
      testProcessedEstate("4000000008")
    }

    "return OK with valid processed payload for 4000000009" in {
      testProcessedEstate("4000000009")
    }

    "return OK with valid processed payload for 4000000010" in {
      testProcessedEstate("4000000010")
    }

    "return OK with valid processed payload for 5000000000" in {
      testProcessedEstate("5000000000")
    }

    "return OK with valid processed payload for 5000000001" in {
      testProcessedEstate("5000000001")
    }

    "return OK with no payload for in processing estates" in {
      testReturnsOtherStatus("1111111111", "In Processing")
    }

    "return OK with no payload for closed estates" in {
      testReturnsOtherStatus("1111111112", "Closed")
    }

    "return OK with no payload for estates pending closure" in {
      testReturnsOtherStatus("1111111113", "Pending Closure")
    }

    "return OK with no payload for estates trusts" in {
      testReturnsOtherStatus("1111111114", "Parked")
    }

    "return OK with no payload for obsoleted estates" in {
      testReturnsOtherStatus("1111111115", "Obsoleted")
    }

    "return OK with no payload for suspended estates" in {
      testReturnsOtherStatus("1111111116", "Suspended")
    }

    "return Bad Request when utr code is not valid" in {
      val resultJson = getEstateAsJson("12345678", BAD_REQUEST)

      (resultJson \ "code").as[String] mustBe "INVALID_UTR"
    }

    "registration not available for provided utr" in {
      val resultJson = getEstateAsJson("0000000404", NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }

    "return Internal Server Error when des having internal errors" in {
      val resultJson = getEstateAsJson("0000000500", INTERNAL_SERVER_ERROR)

      (resultJson \ "code").as[String] mustBe "SERVER_ERROR"
      (resultJson \ "reason").as[String] mustBe "DES is currently experiencing problems that require live service intervention"
    }

    "return 503 service unavailable when dependent service is unavailable" in {
      val resultJson = getEstateAsJson("0000000503", SERVICE_UNAVAILABLE)

      (resultJson \ "code").as[String] mustBe "SERVICE_UNAVAILABLE"
      (resultJson \ "reason").as[String] mustBe "Dependent systems are currently not responding"
    }

  }

  private def getEstateAsJson(utr: String, expectedResult: Int): JsValue = {
    val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
    val result = SUT.getEstate(utr).apply(request)
    status(result) must be(expectedResult)
    contentType(result).get mustBe "application/json"
    contentAsJson(result)
  }

  private def getEstateAsValidatedJson(utr: String): JsValue = {
    val resultJson = getEstateAsJson(utr, OK)

    val validationResult = displayValidator.validateAgainstSchema(resultJson.toString)
    validationResult mustBe SuccessfulValidation

    resultJson
  }

  private def testProcessedEstate(utr: String) = {
    val resultJson = getEstateAsValidatedJson(utr)

    (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Processed"
    (resultJson \ "trustOrEstateDisplay" \ "applicationType").as[String] mustBe "02"
    (resultJson \ "trustOrEstateDisplay" \ "matchData" \ "utr").as[String] mustBe utr
  }

  private def testReturnsOtherStatus(utr: String, status: String) = {
    val resultJson = getEstateAsJson(utr, OK)

    (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe status
    (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
  }
}
