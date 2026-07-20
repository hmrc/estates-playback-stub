/*
 * Copyright 2026 HM Revenue & Customs
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

package utils

import play.api.libs.json.{JsValue, Json}

object HipResponse {

  val jsonResponse500: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "code": "500",
       |    "message": "String",
       |    "logID": "00000000000000000000000000000000"
       |  }
       |}
       |""".stripMargin)

  val jsonResponse400: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "code": "400",
       |    "message": "String",
       |    "logID": "00000000000000000000000000000000"
       |  }
       |}
       |""".stripMargin)

  val jsonResponseResourceNotFound: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "processingDate": "2026-01-31T09:26:17Z",
       |    "errorId": "000",
       |    "text": "UTR or URN is invalid"
       |  }
       |}
       |""".stripMargin)

  val jsonResponseTechnicalError: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "processingDate": "2026-01-31T09:26:17Z",
       |    "errorId": "999",
       |    "text": "Technical Error"
       |  }
       |}
       |""".stripMargin)

  val jsonResponseNotProcessed: JsValue = Json.parse(s"""
       |{
       |  "error": {
       |    "processingDate": "2026-01-31T09:26:17Z",
       |    "errorId": "003",
       |    "text": "Request could not be processed"
       |  }
       |}
       |""".stripMargin)

}
