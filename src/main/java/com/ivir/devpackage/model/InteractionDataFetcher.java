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

package com.ivir.devpackage.model;

import com.ivir.devpackage.api.fed.MmsFederateService;
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToWebCallback;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DgsComponent
public class InteractionDataFetcher{
    public static Logger logger = LoggerFactory.getLogger(InteractionDataFetcher.class);
    private Storage controlInteractionStorage;
    private Storage restInteractionStorage;
    private Storage medicalEvacuationInteractionStorage;
    private Storage tcccInteractionInteractionStorage;
    private Storage scenarioInteractionStorage;
    private Storage instructionalInteractionStorage;
    private Storage requestLabInteractionStorage;
    private Storage patientInteractionStorage;
    private Storage magicVitalsInteractionStorage;
    private Storage magicTransferInteractionStorage;
    private Storage vitalsDisplayControlInteractionStorage;

    @Autowired
    StorageService storageService;

    @Autowired
    MmsFederateService mmsFederateService;

    @PostConstruct
    public void init(){
        this.controlInteractionStorage = storageService.getStorage("ControlInteraction");
        this.restInteractionStorage = storageService.getStorage("RestInteraction");
        this.medicalEvacuationInteractionStorage = storageService.getStorage("MedicalEvacuationInteraction");
        this.tcccInteractionInteractionStorage = storageService.getStorage("TcccInteraction");
        this.scenarioInteractionStorage = storageService.getStorage("ScenarioInteraction");
        this.instructionalInteractionStorage = storageService.getStorage("InstructionalInteraction");
        this.requestLabInteractionStorage = storageService.getStorage("RequestLabInteraction");
        this.patientInteractionStorage = storageService.getStorage("PatientInteraction");
        this.magicVitalsInteractionStorage = storageService.getStorage("MagicVitalsInteraction");
        this.magicTransferInteractionStorage = storageService.getStorage("MagicTransferInteraction");
        this.vitalsDisplayControlInteractionStorage = storageService.getStorage("VitalsDisplayControlInteraction");
    }

    private void assertFederationIsConnected(){
        if(!mmsFederateService.isConnected()){
            throw new RuntimeException("You must connect to the federation first");
        }
    }

    @DgsData(parentType = "Mutation", field = "sendControlInteraction")
    public String sendControlInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendControlInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = dfe.getArguments();
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",controlEnumToInstanceName(dataMap));
        controlInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> controlEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName",dataMap.get("controlType").toString());
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorControlInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            controlInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendRestInteraction")
    public String sendRestInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendRestInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("restInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",restCallEnumToInstanceName(dataMap));
        restInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> restCallEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName",dataMap.get("callType").toString());
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorRestInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            restInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendMedicalEvacuationInteraction")
    public String sendMedicalEvacuationInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendMedicalEvacuationInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("medicalEvacuationInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",restMedicalEvacuationTypeEnumToInstanceName(dataMap));
        medicalEvacuationInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> restMedicalEvacuationTypeEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName",dataMap.get("medicalEvacuationType").toString());
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorMedicalEvacuationInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            medicalEvacuationInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendTcccInteraction")
    public String sendTcccInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendTcccInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("tcccInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",tcccInteractionTypeEnumToInstanceName(dataMap));
        tcccInteractionInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> tcccInteractionTypeEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName",dataMap.get("tcccType").toString());
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorTcccInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            tcccInteractionInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendSelectScenarioInteraction")
    public String sendSelectScenarioInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendSelectScenarioInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("selectScenarioInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",selectScenarioToInstanceName());
        scenarioInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> selectScenarioToInstanceName(){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName","SELECT");
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorSelectScenarioInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            scenarioInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendInstructionalInteraction")
    public String sendInstructionalInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendInstructionalInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("instructionalInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",instructionalEnumToInstanceName(dataMap));
        instructionalInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> instructionalEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName",dataMap.get("instructionalType").toString());
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorInstructionalInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            instructionalInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendRequestLabInteraction")
    public String sendRequestLabInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendRequestLabInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("requestLabInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",requestTypeToInstanceName());
        requestLabInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> requestTypeToInstanceName(){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName","REQUEST");
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorRequestLabInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            requestLabInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendPatientInteraction")
    public String sendPatientInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendPatientInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("patientInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",patientTypeEnumToInstanceName(dataMap));
        patientInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> patientTypeEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName",dataMap.get("patientType").toString());
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorPatientInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            patientInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendMagicVitalsInteraction")
    public String sendMagicVitalsInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendMagicVitalsInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("magicVitalsInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",magicVitalsTypeEnumToInstanceName(dataMap));
        magicVitalsInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> magicVitalsTypeEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName","MagicVitals");
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorMagicVitalsInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            magicVitalsInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendMagicTransferInteraction")
    public String sendMagicTransferInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendMagicTransferInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("magicTransferInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",magicTransferTypeEnumToInstanceName(dataMap));
        magicTransferInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> magicTransferTypeEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName","MagicTransfer");
        return identifierMap;
    }

    @DgsQuery
    public List<Map<String, Object>> magicTransferInteractionAll(){
        assertFederationIsConnected();
        return magicTransferInteractionStorage.getAllItems().stream().map((webItem) -> webItem.getMap()).toList();
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorMagicTransferInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            magicTransferInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    @DgsData(parentType = "Mutation", field = "sendVitalsDisplayControlInteraction")
    public String sendVitalsDisplayControlInteraction(DataFetchingEnvironment dfe){
        logger.info("mutation: sendVitalsDisplayControlInteraction called");
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String, Object>) dfe.getArguments().get("vitalsDisplayControlInteraction");
        WebItem webItem = new WebItem(dataMap,true);
        webItem.put("itemIdentifier",vitalsDisplayControlTypeEnumToInstanceName(dataMap));
        vitalsDisplayControlInteractionStorage.updateFromWeb(webItem);
        return "done";
    }

    private HashMap<String,Object> vitalsDisplayControlTypeEnumToInstanceName(Map<String, Object> dataMap){
        HashMap<String,Object> identifierMap = new HashMap<>();
        identifierMap.put("instanceName","VitalsDisplayControlType");
        return identifierMap;
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorVitalsDisplayControlInteraction() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            vitalsDisplayControlInteractionStorage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

}
