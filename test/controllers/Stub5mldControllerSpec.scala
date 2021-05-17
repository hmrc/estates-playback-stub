/*
 * Copyright 2021 HM Revenue & Customs
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

  "Stub5mldController getEstate" should {

    "return 200 with a valid response payload for a taxable estate with UTR 2000000000" in {
      val result = getEstateForUtr("2000000000")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 2000000001" in {
      val result = getEstateForUtr("2000000001")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 2000000003" in {
      val result = getEstateForUtr("2000000003")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 2000000004" in {
      val result = getEstateForUtr("2000000004")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000000" in {
      val result = getEstateForUtr("4000000000")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000001" in {
      val result = getEstateForUtr("4000000001")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000002" in {
      val result = getEstateForUtr("4000000002")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000003" in {
      val result = getEstateForUtr("4000000003")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000004" in {
      val result = getEstateForUtr("4000000004")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000005" in {
      val result = getEstateForUtr("4000000005")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000006" in {
      val result = getEstateForUtr("4000000006")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000007" in {
      val result = getEstateForUtr("4000000007")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000008" in {
      val result = getEstateForUtr("4000000008")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000009" in {
      val result = getEstateForUtr("4000000009")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 4000000010" in {
      val result = getEstateForUtr("4000000010")
      status(result) must be(OK)
    }


    "return 200 with a valid response payload for a taxable estate with UTR 2500000000" in {
      val result = getEstateForUtr("2500000000")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 2500000001" in {
      val result = getEstateForUtr("2500000001")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 2500000002" in {
      val result = getEstateForUtr("2500000002")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for a taxable estate with UTR 2500000003" in {
      val result = getEstateForUtr("2500000003")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 2500000004" in {
      val result = getEstateForUtr("2500000004")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 2500000005" in {
      val result = getEstateForUtr("2500000005")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 2500000007" in {
      val result = getEstateForUtr("2500000007")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 2500000010" in {
      val result = getEstateForUtr("2500000010")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 2500000101" in {
      val result = getEstateForUtr("2500000101")
      status(result) must be(OK)
    }

    "return 200 with a valid response payload for estate with UTR 2500000102" in {
      val result = getEstateForUtr("2500000102")
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
