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

package controllers

import controllers.des.DesHeaderValidator
import play.api.mvc.{AnyContent, Request, Result}

import scala.concurrent.Future

trait StubBaseController extends DesHeaderValidator {

  val utrRegex = "^[0-9]{10}$".r

  def json5mldResult(id: String)(implicit request: Request[AnyContent]): Future[Result] =
    jsonResult(s"5mld/$id")

  def jsonResult(utr: String)(implicit request: Request[AnyContent]): Future[Result]

  def is5mldIdValid(id: String): Boolean =
    utrRegex.findFirstIn(id).isDefined

}
