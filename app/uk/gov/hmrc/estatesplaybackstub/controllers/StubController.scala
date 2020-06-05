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

  def getTrusts(utr: String): Action[AnyContent] = headerValidatorAction.async {
    implicit request =>
    if (isUtrValid(utr)) {
      utr match {

        //estates
        case "2000000000" | "2000000001" | "2000000002" | "2000000003" | "2000000004" =>
          jsonResult(utr, Ok)

        case "4000000000" | "4000000001" | "4000000002" | "4000000003" | "4000000004" | "4000000005" | "4000000006" | "4000000007"
             | "4000000008" | "4000000009" | "4000000010" =>
          jsonResult(utr, Ok)

        // In Processing, Closed, Pending Closure, Parked, Obsoleted and Suspended
        case "1111111111" | "1111111112" | "1111111113" | "1111111114" | "1111111115" | "1111111116" =>
          jsonResult(utr, Ok)

        case "0000000500" =>
          Future.successful(InternalServerError(jsonResponse500))
        case "0000000503" =>
          Future.successful(ServiceUnavailable(jsonResponse503))
        case _ => Future.successful(NotFound(jsonResponseResourceNotFound))
      }
    } else {
      Future.successful(BadRequest(jsonResponseInvalidUtr))
    }
  }

}
