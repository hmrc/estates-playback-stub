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

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.estatesplaybackstub.controllers.actions.HeaderValidatorAction
import uk.gov.hmrc.estatesplaybackstub.utils.DesResponse._

import scala.concurrent.{ExecutionContext, Future}

@Singleton()
class StubController @Inject()(headerValidatorAction: HeaderValidatorAction)
                              (implicit ec: ExecutionContext, cc: ControllerComponents) extends StubBaseController {

  //noinspection ScalaStyle
  def getEstate(utr: String): Action[AnyContent] = headerValidatorAction.async {
    implicit request =>
    if (isUtrValid(utr)) {
      utr match {
        case "2000000000" => jsonResult(utr, Ok)
        case "2000000001" => jsonResult(utr, Ok)
        case "2000000002" => jsonResult(utr, Ok)
        case "2000000003" => jsonResult(utr, Ok)
        case "2000000004" => jsonResult(utr, Ok)
        case "4000000000" => jsonResult(utr, Ok)
        case "4000000001" => jsonResult(utr, Ok)
        case "4000000002" => jsonResult(utr, Ok)
        case "4000000003" => jsonResult(utr, Ok)
        case "4000000004" => jsonResult(utr, Ok)
        case "4000000005" => jsonResult(utr, Ok)
        case "4000000006" => jsonResult(utr, Ok)
        case "4000000007" => jsonResult(utr, Ok)
        case "4000000008" => jsonResult(utr, Ok)
        case "4000000009" => jsonResult(utr, Ok)
        case "4000000010" => jsonResult(utr, Ok)
        case "1111111111" => jsonResult(utr, Ok)  // In Processing
        case "1111111112" => jsonResult(utr, Ok)  // Closed
        case "1111111113" => jsonResult(utr, Ok)  // Pending Closure
        case "1111111114" => jsonResult(utr, Ok)  // Parked
        case "1111111115" => jsonResult(utr, Ok)  // Obsoleted
        case "1111111116" => jsonResult(utr, Ok)  // Suspended
        case "5000000000" => jsonResult(utr, Ok)  // Suspended
        case "0000000500" => Future.successful(InternalServerError(jsonResponse500))
        case "0000000503" => Future.successful(ServiceUnavailable(jsonResponse503))
        case _ => Future.successful(NotFound(jsonResponseResourceNotFound))
      }
    } else {
      Future.successful(BadRequest(jsonResponseInvalidUtr))
    }
  }
}
