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

package service

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.networknt.schema.{Error, InputFormat, Schema, SchemaRegistry, SpecificationVersion}
import models.{DesValidationError, FailedValidation, SuccessfulValidation, ValidationResult}
import play.api.Logging

import java.io.InputStream
import scala.io.Source
import scala.jdk.CollectionConverters.IterableHasAsScala

class ValidationService() {

  def get(schemaFile: String): Validator = {
    val resource = resourceAsString(schemaFile)
      .getOrElse(throw new RuntimeException("Missing schema: " + schemaFile))

    val schema = SchemaRegistry
      .withDefaultDialect(SpecificationVersion.DRAFT_4)
      .getSchema(resource)
    new Validator(schema)
  }

  private def resourceAsString(resourcePath: String): Option[String] =
    resourceAsInputStream(resourcePath) map { is =>
      Source.fromInputStream(is).getLines().mkString("\n")
    }

  private def resourceAsInputStream(resourcePath: String): Option[InputStream] =
    Option(getClass.getResourceAsStream(resourcePath))

}

class Validator(schema: Schema) extends Logging {

  private def validateInternal(subject: String): List[Error] =
    schema.validate(subject, InputFormat.JSON).asScala.toList

  def validateAgainstSchema(input: String): ValidationResult =

    try {
      val json: JsonNode                = doNotAllowDuplicatedProperties(input)
      val validationOutput: List[Error] = validateInternal(json.toString)

      if (validationOutput.isEmpty) {
        SuccessfulValidation
      } else {
        val validationErrors = getValidationErrors(validationOutput)
        val failedValidation = FailedValidation("Invalid Json", 0, validationErrors)
        logger.info(s"[Validator][validateAgainstSchema] validation errors: ${validationErrors.mkString}")
        logger.info(s"[Validator][validateAgainstSchema] Failed schema validation: ${failedValidation.toString}")
        failedValidation
      }
    } catch {
      case ex: Exception =>
        logger.error(
          s"[Validator][validateAgainstSchema] Error validating Json request against Schema: ${ex.getMessage}"
        )
        FailedValidation("Not JSON", 0, Nil)
    }

  private def getValidationErrors(validationOutput: List[Error]): Seq[DesValidationError] =
    validationOutput.map { error =>
      val message  = error.getMessage
      val location = error.getInstanceLocation.toString
      logger.error(s"[Validator][getValidationErrors] Failed at locations : $location")
      DesValidationError(message, if (location == "") "/" else location)
    }

  private def doNotAllowDuplicatedProperties(jsonNodeAsString: String): JsonNode = {
    val objectMapper: ObjectMapper = new ObjectMapper()
    objectMapper.enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)
    objectMapper.readTree(jsonNodeAsString)

  }

}
