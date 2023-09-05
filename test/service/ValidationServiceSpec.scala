/*
 * Copyright 2023 HM Revenue & Customs
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

package service

import controllers.SpecBase
import models.{FailedValidation, SuccessfulValidation}
import utils.JsonUtils

class ValidationServiceSpec extends SpecBase {

  private val displayValidator5mld = new ValidationService().get(
    "/resources/schemas/5mld/display-trust-or-estate-4.1.0.json"
  )

  "ValidationService " should {
    "return success validation for valid 5mld json utr 2000000000" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2000000000")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000000" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000000")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000001" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000001")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000002" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000002")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000003" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000003")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000004" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000004")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000005" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000005")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000007" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000007")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000010" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000010")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000101" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000101")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }

    "return success validation for valid 5mld json utr 2500000102" in {
      val jsonString = JsonUtils.jsonFromFile(get5mldPath("2500000102")).toString()
      val validationResult = displayValidator5mld.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }


    "return failed validation for not json" in {
      val validationResult = displayValidator5mld.validateAgainstSchema("Fred")
      validationResult mustBe FailedValidation("Not JSON", 0, Nil)
    }
    "return failed validation for duplicate properties" in {
      val json =
        """
          |{
          | "field": 1,
          | "field": 2
          |}
          |""".stripMargin
      val validationResult = displayValidator5mld.validateAgainstSchema(json)
      validationResult mustBe FailedValidation("Not JSON", 0, Nil)
    }
    "return failed validation for invalid json" in {
      val validationResult = displayValidator5mld.validateAgainstSchema("{}")
      val result = validationResult.asInstanceOf[FailedValidation]
      result mustNot be(null)
      result.message mustBe "Invalid Json"
    }

  }

  private def get5mldPath(utr: String): String = {
    s"/resources/5mld/$utr.json"
  }
}
