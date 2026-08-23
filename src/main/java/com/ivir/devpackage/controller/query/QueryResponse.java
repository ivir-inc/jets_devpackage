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

public class QueryResponse {
    private HashMap<String, Object> data;
    private List<QueryResponseError> errors;

    public HashMap<String, Object> getData() {
        return data;
    }

    public QueryResponse setData(HashMap<String, Object> data) {
        this.data = data;
        return this;
    }

    public List<QueryResponseError> getErrors() {
        return errors;
    }

    public QueryResponse setErrors(List<QueryResponseError> errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "data=" + data +
                ", errors=" + errors +
                '}';
    }
}
