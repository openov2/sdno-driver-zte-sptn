/*
 * Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.openo.sdno.sptndriver.services;

import org.openo.sdno.sptndriver.models.south.SCommandResult;
import org.openo.sdno.sptndriver.models.south.SCreateElineAndTunnels;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface ISElineService {

  @POST("restconf/operations/sptn-service-eline:create-snc-eline-tunnels")
  Call<SCommandResult> createElineAndTunnels(@Body SCreateElineAndTunnels createElineAndTunnels);

  @DELETE("restconf/operations/sptn-service-eline:delete-snc-eline")
  Call<Response> deleteEline(@Body String elineId);
}
