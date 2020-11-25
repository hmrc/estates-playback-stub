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

import play.api.libs.json.JsObject
import play.api.test.Helpers._

class Stub5mldControllerSpec extends SpecBase {

  private val SUT = app.injector.instanceOf[Stub5mldController]

  private def createRequestForUtr(utr: String) = createGetRequestWithValidHeaders(s"/trusts/registration/UTR/$utr")
  private def createRequestForUrn(urn: String) = createGetRequestWithValidHeaders(s"/trusts/registration/URN/$urn")

  private def getEstateForUtr(utr: String) = {
    val request = createRequestForUtr(utr)
    SUT.getEstate(utr).apply(request)
  }

  private def getEstateForUrn(urn: String) = {
    val request = createRequestForUrn(urn)
    SUT.getEstate(urn).apply(request)
  }

  "Stub5mldController getEstate" should {

    "return 200 with a valid response payload for estates with no identification for beneficiaries" in {
      val result = getEstateForUtr("5174384721")
      status(result) must be(OK)

      val jsonResponse = contentAsJson(result)

      (jsonResponse \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Processed"

      (jsonResponse \ "trustOrEstateDisplay").toOption must be(defined)
      (jsonResponse \ "trustOrEstateDisplay" \ "applicationType").as[String] mustBe "01"

      (jsonResponse \ "trustOrEstateDisplay" \ "details" \ "trust" \ "entities" \ "trustees").as[List[JsObject]].size mustBe 25
    }

    "return 200 with a valid response payload for a non-taxable estate with URN 0000000004AAAAA" in {
      val result = getEstateForUrn("0000000004AAAAA")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 1000000001" in {
      val result = getEstateForUtr("1000000001")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 1000000002" in {
      val result = getEstateForUtr("1000000002")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 1000000003" in {
      val result = getEstateForUtr("1000000003")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 1000000007" in {
      val result = getEstateForUtr("1000000007")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 1000000010" in {
      val result = getEstateForUtr("1000000010")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 1000000008" in {
      val result = getEstateForUtr("1000000008")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 1000000101" in {
      val result = getEstateForUtr("1000000101")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 1000000102" in {
      val result = getEstateForUtr("1000000102")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a non-taxable estate with UTR 1000000103" in {
      val result = getEstateForUtr("1000000103")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with URN" in {
      val result = getEstateForUrn("1234567890AAAAA")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a non-taxable estate with URN 0000000001AAAAA" in {
      val result = getEstateForUrn("0000000001AAAAA")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a non-taxable estate with URN 0000000002AAAAA" in {
      val result = getEstateForUrn("0000000002AAAAA")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a non-taxable estate with URN 0000000003AAAAA" in {
      val result = getEstateForUrn("0000000003AAAAA")
      status(result) must be(OK)
    }

    "return 200 with no payload for in processing estates " in {
      val result = getEstateForUtr("1111111111")
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "In Processing"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)
    }

    "return 200 with no payload for closed estates " in {
      val result = getEstateForUtr("1111111112")
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Closed"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for estates pending closure " in {
      val result = getEstateForUtr("1111111113")
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Pending Closure"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for parked estates " in {
      val result = getEstateForUtr("1111111114")
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Parked"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for obsoleted estates " in {
      val result = getEstateForUtr("1111111115")
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Obsoleted"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 200 with no payload for suspended estates " in {
      val result = getEstateForUtr("1111111116")
      status(result) must be(OK)
      (contentAsJson(result) \ "responseHeader" \ "dfmcaReturnUserStatus")
        .as[String] mustBe "Suspended"

      (contentAsJson(result) \ "trustOrEstateDisplay").toOption mustNot be(defined)

    }

    "return 400 utr code is not valid " in {
      val result = getEstateForUtr("12345678")

      status(result) must be(BAD_REQUEST)
      (contentAsJson(result) \ "code").as[String] mustBe "INVALID_UTR"
      contentType(result).get mustBe "application/json"
    }

    "registration not available for provided utr " in {
      val result = getEstateForUtr("0000000404")

      status(result) must be(NOT_FOUND)
      (contentAsJson(result) \ "code").as[String] mustBe "RESOURCE_NOT_FOUND"
    }

    "return 500 Internal server error when des having internal errors." in {
      val result = getEstateForUtr("0000000500")

      status(result) must be(INTERNAL_SERVER_ERROR)
      (contentAsJson(result) \ "code").as[String] mustBe "SERVER_ERROR"
      (contentAsJson(result) \ "reason").as[String] mustBe "DES is currently experiencing problems that require live service intervention"
    }

    "return 503 service unavailable when dependent service is unavailable" in {
      val result = getEstateForUtr("0000000503")
      status(result) must be(SERVICE_UNAVAILABLE)
      (contentAsJson(result) \ "code").as[String] mustBe "SERVICE_UNAVAILABLE"
      (contentAsJson(result) \ "reason").as[String] mustBe "Dependent systems are currently not responding"
    }
  }
}
