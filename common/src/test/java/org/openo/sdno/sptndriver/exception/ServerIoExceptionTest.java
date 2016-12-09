/*
 * Copyright 2016 ZTE Corporation.
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

package org.openo.sdno.sptndriver.exception;

import org.junit.Test;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

/**
 * The unit test class of ServerIoException.
 */
public class ServerIoExceptionTest {
    @Test
    public void testGetResponse() throws Exception {
        ServerIoException ex = new ServerIoException(new IOException("IO Exception"));

        Response actualValue = ex.getResponse();
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), actualValue.getStatus());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, actualValue.getMediaType());
        assertEquals("java.io.IOException: IO Exception", actualValue.getEntity());
    }

}