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

import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc.{ControllerComponents, Result}
import play.api.test.Helpers._
import uk.gov.hmrc.estatesplaybackstub.controllers.actions.HeaderValidatorAction
import uk.gov.hmrc.estatesplaybackstub.models.SuccessfulValidation
import uk.gov.hmrc.estatesplaybackstub.service.{ValidationService, Validator}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StubControllerSpec extends SpecBase {

  private implicit val cc: ControllerComponents = app.injector.instanceOf[ControllerComponents]
  private val headerValidatorAction: HeaderValidatorAction = app.injector.instanceOf[HeaderValidatorAction]

  private val displayTrustsSchema = "/resources/schemas/display-trusts-3.0.json"
  private val displayValidator = new ValidationService().get(displayTrustsSchema)

  val SUT = new StubController(headerValidatorAction)

  "StubController getTrusts " should {

    "return 200 with valid response payload for estates " in {
      val resultJson = getEstateAsValidatedJson("2000000000")

      val validationResult = displayValidator.validateAgainstSchema(resultJson.toString)
      validationResult mustBe SuccessfulValidation

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Processed"
      (resultJson \ "trustOrEstateDisplay" \ "applicationType").as[String] mustBe "02"
      (resultJson \ "trustOrEstateDisplay" \ "matchData" \ "utr").as[String] mustBe "2000000000"
    }

    "return 200 with no payload for in processing estates " in {
      val resultJson = getEstateAsJson("1111111111", OK)

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "In Processing"
      (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 200 with no payload for closed estates" in {
      val resultJson = getEstateAsJson("1111111112", OK)

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Closed"
      (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 200 with no payload for estates pending closure" in {
      val resultJson = getEstateAsJson("1111111113", OK)

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Pending Closure"
      (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 200 with no payload for estates trusts" in {
      val resultJson = getEstateAsJson("1111111114", OK)

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Parked"
      (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 200 with no payload for obsoleted estates" in {
      val resultJson = getEstateAsJson("1111111115", OK)

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Obsoleted"
      (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 200 with no payload for suspended estates" in {
      val resultJson = getEstateAsJson("1111111116", OK)

      (resultJson \ "responseHeader" \ "dfmcaReturnUserStatus").as[String] mustBe "Suspended"
      (resultJson \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 400 utr code is not valid" in {
      val resultJson = getEstateAsJson("12345678", BAD_REQUEST)

      (resultJson \ "code").as[String] mustBe "INVALID_UTR"
    }

    "registration not available for provided utr" in {
      val resultJson = getEstateAsJson("0000000404", NOT_FOUND)

      (resultJson \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }

    "return 500 Internal server error when des having internal errors" in {
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
}
