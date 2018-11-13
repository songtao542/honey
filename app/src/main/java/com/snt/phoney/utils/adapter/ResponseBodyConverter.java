/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.snt.phoney.utils.adapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    char[] buffer = new char[2 * 1024];

    ResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            Reader reader = value.charStream();
            StringBuffer stringBuffer = new StringBuffer();
            int size;
            while ((size = reader.read(buffer, 0, buffer.length)) != -1) {
                stringBuffer.append(buffer, 0, size);
            }
            String resultString = stringBuffer.toString();
            //JsonReader jsonReader = gson.newJsonReader(value.charStream());
            //return adapter.read(jsonReader);
            return adapter.fromJson(convertToResponseFormat(resultString));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            value.close();
        }
    }

    private String convertToResponseFormat(String resultString) {
        JsonObject jsonObject = gson.fromJson(resultString, JsonObject.class);
        JsonObject result = new JsonObject();
        result.add("code", jsonObject.getAsJsonObject("header").get("code"));
        result.add("message", jsonObject.getAsJsonObject("header").get("msg"));
        Set<Map.Entry<String, JsonElement>> dataSet = jsonObject.getAsJsonObject("body").getAsJsonObject("result").entrySet();
        for (Map.Entry<String, JsonElement> entry : dataSet) {
            result.add("data", entry.getValue());
            break;
        }
        return gson.toJson(result);
    }
}
