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

package controllers.hip

import controllers.Stub5mldController
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import utils.HipResponse.*

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton()
class HipStub5MldController @Inject() (headerValidatorAction: HipHeaderValidatorAction)(implicit
  cc: ControllerComponents
) extends HipStubBaseController with Stub5mldController {

  def getEstate(id: String): Action[AnyContent] = headerValidatorAction.async { implicit request =>
    if (is5mldIdValid(id)) {
      id match {
        case "0000000500" =>
          Future.successful(InternalServerError(jsonResponse500))
        case "0000000999" =>
          Future.successful(UnprocessableEntity(jsonResponseTechnicalError))
        case "0000000003" =>
          Future.successful(UnprocessableEntity(jsonResponseNotProcessed))
        case "0000000404" =>
          Future.successful(UnprocessableEntity(jsonResponseResourceNotFound))
        case _            =>
          successResponses(id)
      }
    } else {
      Future.successful(BadRequest(jsonResponse400))
    }
  }

}
