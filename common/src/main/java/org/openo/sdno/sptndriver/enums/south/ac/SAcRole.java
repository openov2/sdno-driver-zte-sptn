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

package org.openo.sdno.sptndriver.enums.south.ac;

/**
 *  Enum of AC role.
 */
public enum SAcRole {
  MASTER(0),
  SLAVE(1);
  private Integer value;

  SAcRole(Integer value) {
    this.value = value;
  }

  public String toString() {
    return String.valueOf(value);
  }
}
