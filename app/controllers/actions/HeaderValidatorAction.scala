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

package controllers.actions

import javax.inject.Inject
import play.api.Logging
import play.api.mvc.{ActionBuilder, AnyContent, BodyParsers, Request, Result, Results}
import controllers.HeaderValidator
import utils.Session
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class HeaderValidatorAction @Inject()(
                                       override val parser: BodyParsers.Default,
                                       override val executionContext: ExecutionContext)
  extends ActionBuilder[Request, AnyContent] with HeaderValidator with Logging {

  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {

    val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    val (envValid, tokeValid) = (isEnvironmentValid(request), isTokenValid(request))
    logger.info(s"[Session ID: ${Session.id(hc)}] isEnvironmentValid: $envValid, isTokenValid: $tokeValid")
    (envValid, tokeValid) match {
      case (false, false) => Future.successful(Results.Forbidden)
      case (false, true) => Future.successful(Results.Forbidden)
      case (true, false) => Future.successful(Results.Unauthorized)
      case (true, true) =>
        if (isCorrelationIdValid(request)) {
          block(request)
        } else {
          logger.info(s"[Session ID: ${Session.id(hc)}] Correlation-Id is missing or invalid")
          Future.successful(Results.Forbidden)
        }
    }
  }


}
