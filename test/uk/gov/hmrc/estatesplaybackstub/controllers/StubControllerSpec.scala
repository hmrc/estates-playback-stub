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

package uk.gov.hmrc.estatesplaybackstub.controllers

import play.api.libs.json.JsObject
import play.api.mvc.ControllerComponents
import play.api.test.Helpers._
import uk.gov.hmrc.estatesplaybackstub.controllers.actions.HeaderValidatorAction
import uk.gov.hmrc.estatesplaybackstub.models.SuccessfulValidation
import uk.gov.hmrc.estatesplaybackstub.service.{ValidationService, Validator}

import scala.concurrent.ExecutionContext.Implicits.global

class StubControllerSpec extends SpecBase {

  private implicit val cc: ControllerComponents = app.injector.instanceOf[ControllerComponents]
  private val headerValidatorAction: HeaderValidatorAction = app.injector.instanceOf[HeaderValidatorAction]

  private val displayTrustsSchema = "/resources/schemas/display-trusts-3.0.json"
  private val displayValidator = new ValidationService().get(displayTrustsSchema)

  val SUT = new StubController(headerValidatorAction)

  "StubController getTrusts " should {

    "return 200 with valid response payload for estates " in {
      val utr = "2000000000"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      val body = contentAsString(result)

      val validationResult = displayValidator.validateAgainstSchema(body)
      validationResult mustBe SuccessfulValidation

      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Processed"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption must be(defined)
      (contentAsJson(result) \ "trustOrEstateDisplay" \ "applicationType").as[String] mustBe "02"

    }

    "return 200 with no payload for in processing estates " in {
      val utr = "1111111111"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "In Processing"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for closed estates" in {
      val utr = "1111111112"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Closed"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for estates pending closure" in {
      val utr = "1111111113"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Pending Closure"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for estates trusts" in {
      val utr = "1111111114"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Parked"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for obsoleted estates" in {
      val utr = "1111111115"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Obsoleted"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for suspended estates" in {
      val utr = "1111111116"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Suspended"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 400 utr code is not valid " in {
      val utr = "12345678"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)

      status(result) must be(BAD_REQUEST)
      (contentAsJson(result) \ "code").as[String] mustBe "INVALID_UTR"
      contentType(result).get mustBe "application/json"
    }

    "registration not available for provided utr " in {
      val utr = "0000000404"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)

      status(result) must be(NOT_FOUND)
      (contentAsJson(result) \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }

    "return 500 Internal server error when des having internal errors." in {
      val utr = "0000000500"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)

      status(result) must be(INTERNAL_SERVER_ERROR)
      (contentAsJson(result) \ "code").as[String] mustBe "SERVER_ERROR"
      (contentAsJson(result) \ "reason").as[String] mustBe "DES is currently experiencing problems that require live service intervention"
    }

    "return 503 service unavailable when dependent service is unavailable" in {
      val utr = "0000000503"
      val request = createGetRequestWithValidHeaders(s"/trusts/registration/$utr")
      val result = SUT.getTrusts(utr).apply(request)
      status(result) must be(SERVICE_UNAVAILABLE)
      (contentAsJson(result) \ "code").as[String] mustBe "SERVICE_UNAVAILABLE"
      (contentAsJson(result) \ "reason").as[String] mustBe "Dependent systems are currently not responding"
    }

  }

}
