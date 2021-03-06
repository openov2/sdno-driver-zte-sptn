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

package org.openo.sdno.sptndriver.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * The Json util class
 */
public class JsonUtil {

    private JsonUtil(){}

    /**
     * Read Json from file
     * @param fileName Json file name
     * @return Json element
     * @throws IOException
     * @throws ParseException When parse Json fails.
     */
    public static Object readJsonFromFile(String fileName)
        throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return parser.parse(new FileReader(fileName));
    }

    /**
     * Parse Json from file and turn it to specific Java object.
     * @param fileName Json file name
     * @param <T> Excepted Object type
     * @return Java object of type T
     * @throws FileNotFoundException When Json file not exists
     */
    public static <T> T parseJsonFromFile(String fileName,
                                          Type type)
        throws FileNotFoundException {
        File routeCase = new File(fileName);
        JsonParser routeParser = new JsonParser();
        JsonElement routeBody = routeParser.parse(new FileReader(routeCase));

        Gson gson = new Gson();
        return gson.fromJson(routeBody, type);
    }
}
