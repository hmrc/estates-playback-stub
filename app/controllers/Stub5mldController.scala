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

import controllers.actions.HeaderValidatorAction
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import utils.DesResponse._

import scala.concurrent.Future

@Singleton()
class Stub5mldController @Inject()(headerValidatorAction: HeaderValidatorAction)
                                  (implicit cc: ControllerComponents) extends StubBaseController {

  def getEstate(id: String): Action[AnyContent] = headerValidatorAction.async {
    implicit request =>
    if(is5mldIdValid(id)) {
      id match {
        // 5mld taxable estates
        case "2500000000" | "2500000001" | "2500000002" | "2500000003" | "2500000004" | "2500000007" | "2500000101" | "2500000102" | "2500000011" =>
          json5mldResult(id)
        // 5mld estate failed claim feature
        case "2500000005" =>
          json5mldResult(id)
        // 4MLD taxable estate registered, first time played back under 5MLD. User needs to answer additional questions
        case "2500000010" =>
          json5mldResult(id)
        // In Processing, Closed and Pending Closure
        case "1111111111" | "1111111112" | "1111111113" =>
          jsonResult(id)
        // Parked, Obsoleted and Suspended
        case "1111111114" | "1111111115" | "1111111116" =>
          jsonResult(id)
        case "0000000500" =>
          Future.successful(InternalServerError(jsonResponse500))
        case "0000000503" =>
          Future.successful(ServiceUnavailable(jsonResponse503))
        case _ =>
          Future.successful(NotFound(jsonResponseResourceNotFound))
      }
    } else {
      Future.successful(BadRequest(jsonResponseInvalidUtr))
    }
  }

}
