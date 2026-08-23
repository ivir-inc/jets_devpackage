/*
 * Copyright 2026 IVIR Inc.
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

package com.ivir.devpackage.controller.query;

import java.util.HashMap;
import java.util.List;

public class QueryResponseError {
    private String message;
    private List<ErrorLocation> locations;
    private List<String> path;
    private HashMap<String,Object> extensions;

    public String getMessage() {
        return message;
    }

    public QueryResponseError setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<ErrorLocation> getLocations() {
        return locations;
    }

    public QueryResponseError setLocations(List<ErrorLocation> locations) {
        this.locations = locations;
        return this;
    }

    public List<String> getPath() {
        return path;
    }

    public QueryResponseError setPath(List<String> path) {
        this.path = path;
        return this;
    }

    public HashMap<String, Object> getExtensions() {
        return extensions;
    }

    public QueryResponseError setExtensions(HashMap<String, Object> extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public String toString() {
        return "QueryResponseError{" +
                "message='" + message + '\'' +
                ", locations=" + locations +
                ", path=" + path +
                ", extensions=" + extensions +
                '}';
    }
}
