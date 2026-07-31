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

import com.google.inject.Inject
import play.api.mvc.*

import scala.concurrent.{ExecutionContext, Future}

class HipHeaderValidatorAction @Inject() (parser: BodyParsers.Default)(using val ec: ExecutionContext)
    extends ActionBuilderImpl(parser) {

  def validHeaders(request: Request[?]): Boolean = {
    val headers = request.headers

    headers.hasHeader("correlationid") &&
    headers.hasHeader("X-Originating-System") &&
    headers.hasHeader("X-Receipt-Date") &&
    headers.hasHeader("X-Transmitting-System") &&
    headers.hasHeader("Authorization")
  }

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
    if (validHeaders(request)) {
      block(request)
    } else {
      Future.successful(Results.BadRequest)
    }

}
