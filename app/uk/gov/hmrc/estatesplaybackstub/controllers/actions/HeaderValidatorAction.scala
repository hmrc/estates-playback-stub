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

package uk.gov.hmrc.estatesplaybackstub.controllers.actions

import javax.inject.Inject
import play.api.Logger
import play.api.mvc.{ActionBuilder, AnyContent, BodyParsers, Request, Result, Results}
import uk.gov.hmrc.estatesplaybackstub.controllers.HeaderValidator

import scala.concurrent.{ExecutionContext, Future}

class HeaderValidatorAction @Inject()(
                                       override val parser: BodyParsers.Default,
                                       override val executionContext: ExecutionContext)
  extends ActionBuilder[Request, AnyContent] with HeaderValidator {

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    val (envValid, tokeValid) = (isEnvironmentValid(request), isTokenValid(request))
    Logger.info(s"envValid , tokeValid Valid: ${(envValid, tokeValid)}")
    (envValid, tokeValid) match {
      case (false, false) => Future.successful(Results.Forbidden)
      case (false, true) => Future.successful(Results.Forbidden)
      case (true, false) => Future.successful(Results.Unauthorized)
      case (true, true) =>
        if (isCorrelationIdValid(request)) {
          block(request)
        } else {
          Logger.info(s"Correlation-Id is missing or invalid")
          Future.successful(Results.Forbidden)
        }
    }
  }


}
