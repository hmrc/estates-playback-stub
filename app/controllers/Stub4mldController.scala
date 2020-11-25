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

import controllers.actions.HeaderValidatorAction
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import utils.DesResponse._

import scala.concurrent.Future

@Singleton()
class Stub4mldController @Inject()(headerValidatorAction: HeaderValidatorAction)
                                  (implicit cc: ControllerComponents) extends StubBaseController {

  //noinspection ScalaStyle
  def getEstate(utr: String): Action[AnyContent] = headerValidatorAction.async {
    implicit request =>
    if (isUtrValid(utr)) {
      utr match {
        case "2000000000" => json4mldResult(utr)
        case "2000000001" => json4mldResult(utr)
        case "2000000002" => json4mldResult(utr)
        case "2000000003" => json4mldResult(utr)
        case "2000000004" => json4mldResult(utr)
        case "2000000005" => json4mldResult(utr)
        case "4000000000" => json4mldResult(utr)
        case "4000000001" => json4mldResult(utr)
        case "4000000002" => json4mldResult(utr)
        case "4000000003" => json4mldResult(utr)
        case "4000000004" => json4mldResult(utr)
        case "4000000005" => json4mldResult(utr)
        case "4000000006" => json4mldResult(utr)
        case "4000000007" => json4mldResult(utr)
        case "4000000008" => json4mldResult(utr)
        case "4000000009" => json4mldResult(utr)
        case "4000000010" => json4mldResult(utr)
        // In Processing, Closed and Pending Closure
        case "1111111111" | "1111111112" | "1111111113" =>
          jsonResult(utr)
        // Parked, Obsoleted and Suspended
        case "1111111114" | "1111111115" | "1111111116" =>
          jsonResult(utr)
        case "5000000000" => json4mldResult(utr)  // Suspended
        case "5000000001" => json4mldResult(utr)  // Fail tax enrolments
        case "0000000500" => Future.successful(InternalServerError(jsonResponse500))
        case "0000000503" => Future.successful(ServiceUnavailable(jsonResponse503))
        case _ => Future.successful(NotFound(jsonResponseResourceNotFound))
      }
    } else {
      Future.successful(BadRequest(jsonResponseInvalidUtr))
    }
  }
}
