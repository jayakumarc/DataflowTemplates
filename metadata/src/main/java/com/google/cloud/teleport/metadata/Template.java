/*
 * Copyright (C) 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.teleport.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Annotation that marks a root-level Dataflow Template. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(MultiTemplate.class)
public @interface Template {

  /** The name of the template. Can't have spaces. */
  String name();

  /** The description of the template. */
  String displayName();

  /** The description of the template. */
  String[] description();

  String[] requirements() default "";

  /** Container name to stage (required for Flex templates). */
  String flexContainerName() default "";

  String yamlTemplateFile() default "";

  String xlangContainerName() default "";

  /** The category of the template. */
  TemplateCategory category();

  /** If template should be hidden from the UI. */
  boolean hidden() default false;

  /** Skip options that are not used for this template. Used mainly with {@link MultiTemplate}. * */
  String[] skipOptions() default "";

  /**
   * Optional options that are not used for this template. Used mainly with {@link MultiTemplate} or
   * reusable options classes.. *
   */
  String[] optionalOptions() default "";

  /** The external class that holds the template code. */
  Class<?> placeholderClass() default void.class;

  /**
   * The interface that holds options/parameters to be passed. Not mandatory when "blocks" are used
   * for automatic templates.
   */
  Class<?> optionsClass() default void.class;

  Class<?>[] blocks() default void.class;

  Class<?> dlqBlock() default void.class;

  /** An array that specifies the orders. */
  Class<?>[] optionsOrder() default void.class;

  /** Link to the documentation. */
  String documentation() default "";

  /** Contact information for the Template. */
  String contactInformation() default "";

  AdditionalDocumentationBlock[] additionalDocumentation() default {};

  /** Language in which the template is defined. */
  TemplateType type() default TemplateType.JAVA;

  /** Indicates if the template is a streaming pipeline. * */
  boolean streaming() default false;

  /** Indicates if the template supports at-least-once correctness. */
  boolean supportsAtLeastOnce() default false;

  /** Indicates if the template supports exactly-once correctness. */
  boolean supportsExactlyOnce() default true;

  public @interface AdditionalDocumentationBlock {
    String name();

    String[] content() default "";
  }

  /** Languages that are supported by templates. */
  enum TemplateType {
    JAVA,
    PYTHON,
    YAML,
    XLANG
  }

  /** Marker if the template is still in preview / pre-GA. */
  boolean preview() default false;

  /**
   * List of files to include in Template image when building with Dockerfile. Only works for YAML
   * and XLANG types. Must be in the path of the build files, i.e. copied to target folder.
   *
   * <p>Will be copied as such, using Docker command: COPY ${otherFiles} /template/
   */
  String[] filesToCopy() default {};

  StreamingMode defaultStreamingMode() default StreamingMode.UNSPECIFIED;

  /**
   * Set to true if the template is used internally/ for testing purposes and should not be staged
   * or released.
   */
  boolean testOnly() default false;

  /**
   * Set to true to stage the template image without creating a spec file in GCS or generate any
   * documentation.
   */
  boolean stageImageOnly() default false;

  /** Override the entry point for the image. */
  String[] entryPoint() default "";

  enum StreamingMode {
    UNSPECIFIED,
    EXACTLY_ONCE,
    AT_LEAST_ONCE
  }
}
