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

package service

import controllers.SpecBase
import models.{FailedValidation, SuccessfulValidation}
import utils.JsonUtils

class ValidationServiceSpec extends SpecBase {

  val displayTrustsSchema = "/resources/schemas/display-trusts-3.0.json"
  val displayValidator = new ValidationService().get(displayTrustsSchema)

  "ValidationService " should {
    "return success validation for valid json" in {
      val jsonString = JsonUtils.jsonFromFile("/resources/2000000000.json").toString()
      val validationResult = displayValidator.validateAgainstSchema(jsonString)
      validationResult mustBe SuccessfulValidation
    }
    "return failed validation for not json" in {
      val validationResult = displayValidator.validateAgainstSchema("Fred")
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
      val validationResult = displayValidator.validateAgainstSchema(json)
      validationResult mustBe FailedValidation("Not JSON", 0, Nil)
    }
    "return failed validation for invalid json" in {
      val validationResult = displayValidator.validateAgainstSchema("{}")
      val result = validationResult.asInstanceOf[FailedValidation]
      result mustNot be(null)
      result.message mustBe "Invalid Json"
    }

  }

}