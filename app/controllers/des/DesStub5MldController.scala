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

package controllers.des

import controllers.Stub5mldController
import controllers.actions.DesHeaderValidatorAction
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import utils.DesResponse.*

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton()
class DesStub5MldController @Inject() (desHeaderValidatorAction: DesHeaderValidatorAction)(implicit
  cc: ControllerComponents
) extends DesStubBaseController with Stub5mldController {

  def getEstate(id: String): Action[AnyContent] = desHeaderValidatorAction.async { implicit request =>
    if (is5mldIdValid(id)) {
      id match {
        case "0000000500" =>
          Future.successful(InternalServerError(jsonResponse500))
        case "0000000503" =>
          Future.successful(ServiceUnavailable(jsonResponse503))
        case "0000000404" =>
          Future.successful(NotFound(jsonResponseResourceNotFound))
        case _            =>
          successResponses(id)
      }
    } else {
      Future.successful(BadRequest(jsonResponseInvalidUtr))
    }
  }

}
