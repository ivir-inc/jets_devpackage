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

package com.ivir.devpackage.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivir.devpackage.controller.query.QueryRequest;
import com.ivir.devpackage.controller.query.QueryRequestBuilder;
import com.ivir.devpackage.controller.query.QueryResponse;
import com.ivir.devpackage.controller.query.QueryType;
import com.ivir.devpackage.controller.query.types.EventInput;
import com.ivir.devpackage.controller.query.types.PhysiologyInput;
import com.ivir.devpackage.controller.query.types.VitalSignsInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RestController
public class ControlWebClient {
    Logger logger = LoggerFactory.getLogger(ControlWebClient.class);
    private HttpClient httpClient;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void setup(){
        logger.info("Test");
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    private CompletableFuture<QueryResponse> sendRequest(QueryRequest queryRequest){
        HttpRequest httpRequest = null;
        try {
            String requestString = objectMapper.writeValueAsString(queryRequest);
            logger.info("Sending request: {}", requestString);
            httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/graphql"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestString))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply((response)->{
                    try {
                        logger.info("got response: {}", response.body());
                        return objectMapper.readValue(response.body(),QueryResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletableFuture<QueryResponse> queryVersion(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.QUERY)
                .query("version")
                .addField("appVersion")
                .addField("fomVersion")
                .build()
        );
    }

    public CompletableFuture<QueryResponse> queryFederateList(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.QUERY)
                .query("federateAll")
                .addField("name")
                .build()
        );
    }

    public CompletableFuture<QueryResponse> queryEventList(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.QUERY)
                .query("eventAll")
                .addField("time")
                .addField("simTime")
                .addField("type")
                .addField("source")
                .addField("patientId")
                .addField("learnerId")
                .addField("instructorId")
                .addField("teamId")
                .addField("trainingFacilityId")
                .addField("notes")
                .addField("description")
                .build()
        );
    }

    public CompletableFuture<QueryResponse> mutateJoin(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.MUTATION)
                .query("joinFederation")
                .addVariable("federateName", "String", "JetsDevKit")
                .build()
        );
    }

    public CompletableFuture<QueryResponse> mutateCreateEvent(EventInput eventInput){
        QueryRequestBuilder queryRequestBuilder = QueryRequestBuilder.create()
                .type(QueryType.MUTATION)
                .query("eventUpdate")
                .addVariable("event", "EventInput", eventInput);
        return sendRequest(queryRequestBuilder.build());
    }

    public CompletableFuture<QueryResponse> mutateDisconnect(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.MUTATION)
                .query("resignFromFederation")
                .build()
        );
    }

    public CompletableFuture<QueryResponse> queryPhysiologyList(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.QUERY)
                .query("physiologyAll")
                .addField("patientId")
                .addField("heartRate")
                .addField("diastolicBloodPressure")
                .addField("systolicBloodPressure")
                .addField("peripheralOxygenSaturation")
                .addField("temperatureFahrenheit")
                .addField("respirationEndTidalCarbonDioxide")
                .addField("respirationRate")
                .build()
        );
    }

    public CompletableFuture<QueryResponse> queryVitalSignsList() {
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.QUERY)
                .query("vitalSignsAll")
                .addField("patientId")
                .addField("heartRate")
                .addField("diastolicBloodPressure")
                .addField("systolicBloodPressure")
                .addField("peripheralOxygenSaturation")
                .addField("temperatureFahrenheit")
                .addField("respirationEndTidalCarbonDioxide")
                .addField("respirationRate")
                .build()
        );
    }


    public CompletableFuture<QueryResponse> mutateCreatePhysiology(PhysiologyInput physiologyInput){
        QueryRequestBuilder queryRequestBuilder = QueryRequestBuilder.create()
                .type(QueryType.MUTATION)
                .query("physiologyUpdate")
                .addVariable("physiology", "PhysiologyInput", physiologyInput);
        return sendRequest(queryRequestBuilder.build());
    }

    public CompletableFuture<QueryResponse> mutateCreateVitalSigns(VitalSignsInput vitalSignsInput) {
        QueryRequestBuilder queryRequestBuilder = QueryRequestBuilder.create()
                .type(QueryType.MUTATION)
                .query("vitalSignsUpdate")
                .addVariable("vitalSigns", "VitalSignsInput", vitalSignsInput);
        return sendRequest(queryRequestBuilder.build());
    }

    public CompletableFuture<QueryResponse> getConnectionStatus(){
        return sendRequest(QueryRequestBuilder.create()
                .type(QueryType.QUERY)
                .query("connectionStatus")
                .build()
        );
    }


}
