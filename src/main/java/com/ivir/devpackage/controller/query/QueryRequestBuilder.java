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

import java.util.ArrayList;
import java.util.HashMap;

public class QueryRequestBuilder {
    private QueryType queryType;
    private String query;
    private ArrayList<String> fields = new ArrayList<>();
    private ArrayList<QueryVariable> variables = new ArrayList<>();

    private QueryRequestBuilder(){

    }

    public static QueryRequestBuilder create(){
        return new QueryRequestBuilder();
    }

    public QueryRequestBuilder query(String query){
        this.query = query;
        return this;
    }

    public QueryRequestBuilder type(QueryType qType){
        this.queryType = qType;
        return this;
    }

    public QueryRequestBuilder addField(String field){
        this.fields.add(field);
        return this;
    }

    public QueryRequestBuilder addVariable(String name, String type, Object value){
        QueryVariable qVar = new QueryVariable();
        qVar.setName(name);
        qVar.setType(type);
        qVar.setValue(value);
        this.variables.add(qVar);
        return this;
    }

    public QueryRequestBuilder addVariable(QueryVariable qVar){
        this.variables.add(qVar);
        return this;
    }

    public QueryRequest build(){
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.queryType.getTypeText()).append(" ");
        if((variables != null) && (!variables.isEmpty())){
            stringBuilder.append("(").append(String.join(",",variableDeclarations())).append(") ");
        }
        stringBuilder.append("{").append(query);
        if((variables != null) && (!variables.isEmpty())) {
            stringBuilder.append("(").append(String.join(",",variableAssignments())).append(") ");
        }
        if((fields != null) && (!fields.isEmpty())){
            stringBuilder.append("{").append(String.join(" ", fields)).append("}");
        }
        stringBuilder.append("}");

        queryRequest.setQuery(stringBuilder.toString());

        if((variables != null) && (!variables.isEmpty())) {
            queryRequest.setVariables(variableValues());
        }

        return queryRequest;
    }

    private ArrayList<String> variableDeclarations(){
        ArrayList<String> fieldList = new ArrayList<>();
        for(QueryVariable var : variables){
            fieldList.add("$" + var.getName() + ": " + var.getType());
        }
        return fieldList;
    }

    private ArrayList<String> variableAssignments(){
        ArrayList<String> fieldList = new ArrayList<>();
        for(QueryVariable var : variables){
            fieldList.add(var.getName() + ": $" + var.getName());
        }
        return fieldList;
    }

    private HashMap<String, Object> variableValues(){
        HashMap<String, Object> varMap = new HashMap<>();
        for(QueryVariable var : variables){
            varMap.put(var.getName(),var.getValue());
        }
        return varMap;
    }


}
