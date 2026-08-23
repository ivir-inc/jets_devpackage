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

public class QueryRequest {
    private String query;
    private HashMap<String, Object> variables;

    public String getQuery() {
        return query;
    }

    public QueryRequest setQuery(String query) {
        this.query = query;
        return this;
    }

    public HashMap<String, Object> getVariables() {
        return variables;
    }

    public QueryRequest setVariables(HashMap<String, Object> variables) {
        this.variables = variables;
        return this;
    }

    @Override
    public String toString() {
        return "QueryRequest{" +
                "query='" + query + '\'' +
                ", variables=" + variables +
                '}';
    }
}
