/*
 * Copyright 2016 ZTE, Inc. and others.
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

package org.openo.sdno.sptndriver.config;


import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

import io.dropwizard.Configuration;

/**
 * Configuration class of SDN-O SPTN driver.
 */
public class Config extends Configuration {

  public static final String CONTROLLER_ICT_AUTH =
      "ICTAuthentication:21232F297A57A5A743894A0E4A801FC3";
  @JsonProperty
  @NotEmpty
  private String controllerUrl;

  /**
   * Get controller url from configuration file.
   *
   * @return controller base url
   */
  public String getControllerUrl() {
    return controllerUrl;
  }

}
