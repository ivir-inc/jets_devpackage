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

package com.ivir.devpackage.fed;

import com.ivir.devpackage.model.Storage;
import com.ivir.devpackage.model.StorageService;
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import devstudio.generatedcode.*;
import devstudio.generatedcode.datatypes.MagicVitalsEnum;
import devstudio.generatedcode.datatypes.MedicalEvacuationStateEnum;
import devstudio.generatedcode.datatypes.TransportTypeEnum;
import devstudio.generatedcode.datatypes.VisibleVitalSignEnum;
import devstudio.generatedcode.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.function.Consumer;

public class InteractionFedListener implements HlaInteractionListener{
    private static final Logger logger = LoggerFactory.getLogger(InteractionFedListener.class);
    private HlaInteractionManager interactionManager;

    private Storage controlInteractionStorage;
    private Storage restInteractionStorage;
    private Storage medicalEvacuationInteractionStorage;
    private Storage tcccInteractionStorage;
    private Storage scenarioInteractionStorage;
    private Storage instructionalInteractionStorage;
    private Storage requestLabInteractionStorage;
    private Storage patientInteractionStorage;
    private Storage magicVitalsInteractionStorage;
    private Storage magicTransferInteractionStorage;
    private Storage vitalsDisplayControlInteractionStorage;

    private int idCounter = 0;

    public InteractionFedListener(StorageService storageService, HlaInteractionManager interactionManager){
        this.interactionManager = interactionManager;

        this.controlInteractionStorage = storageService.getStorage("ControlInteraction");
        this.controlInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendControlToHla));

        this.restInteractionStorage = storageService.getStorage("RestInteraction");
        this.restInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendRestToHla));

        this.medicalEvacuationInteractionStorage = storageService.getStorage("MedicalEvacuationInteraction");
        this.medicalEvacuationInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendMedicalEvacuationToHla));

        this.tcccInteractionStorage = storageService.getStorage("TcccInteraction");
        this.tcccInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendTcccToHla));

        this.scenarioInteractionStorage = storageService.getStorage("ScenarioInteraction");
        this.scenarioInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendScenarioToHla));

        this.instructionalInteractionStorage = storageService.getStorage("InstructionalInteraction");
        this.instructionalInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendInstructionalToHla));

        this.requestLabInteractionStorage = storageService.getStorage("RequestLabInteraction");
        this.requestLabInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendRequestLabToHla));

        this.patientInteractionStorage = storageService.getStorage("PatientInteraction");
        this.patientInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendPatientToHla));

        this.magicVitalsInteractionStorage = storageService.getStorage("MagicVitalsInteraction");
        this.magicVitalsInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendMagicVitalsToHla));

        this.magicTransferInteractionStorage = storageService.getStorage("MagicTransferInteraction");
        this.magicTransferInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendMagicTransferToHla));

        this.vitalsDisplayControlInteractionStorage = storageService.getStorage("VitalsDisplayControlInteraction");
        this.vitalsDisplayControlInteractionStorage.setWebItemToHlaCallback(new InteractionWebItemToHlaCallback(this::sendVitalsDisplayControlToHla));
    }

    @Override
    public void start(boolean local, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            controlInteractionStorage.updateFromHla("START", webItem -> {
                webItem.put("controlType","START");
            });
            logger.debug("Received START interaction from HLA.");
        }
    }

    @Override
    public void stop(boolean local, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            controlInteractionStorage.updateFromHla("STOP", webItem -> {
                webItem.put("controlType","STOP");
            });
            logger.debug("Received STOP interaction from HLA.");
        }
    }

    @Override
    public void pause(boolean local, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            controlInteractionStorage.updateFromHla("PAUSE", webItem -> {
                webItem.put("controlType","PAUSE");
            });
            logger.debug("Received PAUSE interaction from HLA.");
        }
    }

    @Override
    public void resume(boolean local, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            controlInteractionStorage.updateFromHla("RESUME", webItem -> {
                webItem.put("controlType","RESUME");
            });
            logger.debug("Received RESUME interaction from HLA.");
        }
    }

    @Override
    public void save(boolean local, HlaInteractionManager.HlaSaveParameters hlaSaveParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            controlInteractionStorage.updateFromHla("SAVE", webItem -> {
                webItem.put("controlType","SAVE");
                if(hlaSaveParameters.hasLabel()){
                    webItem.put("label",hlaSaveParameters.getLabel());
                }
            });
            logger.debug("Received SAVE interaction from HLA: {}", hlaSaveParameters);
        }
    }

    @Override
    public void restore(boolean local, HlaInteractionManager.HlaRestoreParameters hlaRestoreParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            controlInteractionStorage.updateFromHla("RESTORE", webItem -> {
                webItem.put("controlType","RESTORE");
                if(hlaRestoreParameters.hasLabel()){
                    webItem.put("label",hlaRestoreParameters.getLabel());
                }
            });
            logger.debug("Received RESTORE interaction from HLA: {}", hlaRestoreParameters);
        }
    }

    @Override
    public void setTimeScale(boolean b, HlaInteractionManager.HlaSetTimeScaleParameters hlaSetTimeScaleParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        //new
        throw new RuntimeException("need to implement");
    }

    @Override
    public void restCall(boolean local, HlaInteractionManager.HlaRestCallParameters hlaRestCallParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            restInteractionStorage.updateFromHla("CALL", webItem -> {
                webItem.put("callType","CALL");
                if(hlaRestCallParameters.hasCallMethod()){
                    webItem.put("callMethod", hlaRestCallParameters.getCallMethod());
                }
                if(hlaRestCallParameters.hasFromId()){
                    webItem.put("fromId", hlaRestCallParameters.getFromId());
                }
                if(hlaRestCallParameters.hasPath()){
                    webItem.put("path", hlaRestCallParameters.getPath());
                }
                if(hlaRestCallParameters.hasPayload()){
                    webItem.put("payload", hlaRestCallParameters.getPayload());
                }
                if(hlaRestCallParameters.hasToId()){
                    webItem.put("toId", hlaRestCallParameters.getToId());
                }
                if(hlaRestCallParameters.hasMessageId()){
                    webItem.put("messageId", hlaRestCallParameters.getMessageId());
                }
            });
            logger.debug("Received CALL interaction from HLA: {}", hlaRestCallParameters);
        }
    }

    @Override
    public void restResponse(boolean local, HlaInteractionManager.HlaRestResponseParameters hlaRestResponseParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            restInteractionStorage.updateFromHla("RESPONSE", webItem -> {
                webItem.put("callType","RESPONSE");
                if(hlaRestResponseParameters.hasResponseCode()){
                    webItem.put("responseCode", hlaRestResponseParameters.getResponseCode());
                }
                if(hlaRestResponseParameters.hasFromId()){
                    webItem.put("fromId", hlaRestResponseParameters.getFromId());
                }
                if(hlaRestResponseParameters.hasPayload()){
                    webItem.put("payload", hlaRestResponseParameters.getPayload());
                }
                if(hlaRestResponseParameters.hasToId()){
                    webItem.put("toId", hlaRestResponseParameters.getToId());
                }
                if(hlaRestResponseParameters.hasMessageId()){
                    webItem.put("messageId", hlaRestResponseParameters.getMessageId());
                }
            });
            logger.debug("Received RESPONSE interaction from HLA: {}", hlaRestResponseParameters);
        }
    }



    @Override
    public void medicalEvacuationRequest(boolean local, HlaInteractionManager.HlaMedicalEvacuationRequestParameters hlaMedicalEvacuationRequestParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            medicalEvacuationInteractionStorage.updateFromHla("REQUEST", webItem -> {
                webItem.put("medicalEvacuationType","REQUEST");
                if(hlaMedicalEvacuationRequestParameters.hasPatientId()){
                    webItem.put("patientId", hlaMedicalEvacuationRequestParameters.getPatientId());
                }
                if(hlaMedicalEvacuationRequestParameters.hasTransportType()){
                    webItem.put("transportType", hlaMedicalEvacuationRequestParameters.getTransportType());
                }
                if(hlaMedicalEvacuationRequestParameters.hasFacilityId()){
                    webItem.put("facilityId", hlaMedicalEvacuationRequestParameters.getFacilityId());
                    //rel2 backwards compatibility
                    webItem.put("siteName", hlaMedicalEvacuationRequestParameters.getFacilityId());
                }
            });
            logger.debug("Received Medical Evacuation REQUEST interaction: {}", hlaMedicalEvacuationRequestParameters);
        }
    }

    @Override
    public void medicalEvacuationUpdate(boolean local, HlaInteractionManager.HlaMedicalEvacuationUpdateParameters hlaMedicalEvacuationUpdateParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            medicalEvacuationInteractionStorage.updateFromHla("UPDATE", webItem -> {
                webItem.put("medicalEvacuationType","UPDATE");
                if(hlaMedicalEvacuationUpdateParameters.hasPatientId()){
                    webItem.put("patientId", hlaMedicalEvacuationUpdateParameters.getPatientId());
                }
                if(hlaMedicalEvacuationUpdateParameters.hasMedicalEvacuationState()){
                    webItem.put("medicalEvacuationState", hlaMedicalEvacuationUpdateParameters.getMedicalEvacuationState());
                }
                if(hlaMedicalEvacuationUpdateParameters.hasVehicleId()){
                    webItem.put("vehicleId", hlaMedicalEvacuationUpdateParameters.getVehicleId());
                }
                if(hlaMedicalEvacuationUpdateParameters.hasFacilityId()){
                    webItem.put("facilityId", hlaMedicalEvacuationUpdateParameters.getFacilityId());
                    //rel2 backwards compatibility
                    webItem.put("siteName", hlaMedicalEvacuationUpdateParameters.getFacilityId());
                }
            });
            logger.debug("Received Medical Evacuation UPDATE interaction: {}", hlaMedicalEvacuationUpdateParameters);
        }
    }

    @Override
    public void medicalEvacuationResponse(boolean local, HlaInteractionManager.HlaMedicalEvacuationResponseParameters hlaMedicalEvacuationResponseParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            medicalEvacuationInteractionStorage.updateFromHla("RESPONSE", webItem -> {
                webItem.put("medicalEvacuationType","RESPONSE");
                if(hlaMedicalEvacuationResponseParameters.hasPatientId()){
                    webItem.put("patientId", hlaMedicalEvacuationResponseParameters.getPatientId());
                }
                if(hlaMedicalEvacuationResponseParameters.hasMedicalEvacuationState()){
                    webItem.put("medicalEvacuationState", hlaMedicalEvacuationResponseParameters.getMedicalEvacuationState());
                }
                if(hlaMedicalEvacuationResponseParameters.hasVehicleId()){
                    webItem.put("vehicleId", hlaMedicalEvacuationResponseParameters.getVehicleId());
                }
                if(hlaMedicalEvacuationResponseParameters.hasFacilityId()){
                    webItem.put("facilityId", hlaMedicalEvacuationResponseParameters.getFacilityId());
                    //rel2 backwards compatibility
                    webItem.put("siteName", hlaMedicalEvacuationResponseParameters.getFacilityId());
                }
            });
            logger.debug("Received Medical Evacuation RESPONSE interaction: {}", hlaMedicalEvacuationResponseParameters);
        }
    }

    @Override
    public void requestTCCC(boolean local, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            tcccInteractionStorage.updateFromHla("REQUEST", webItem -> {
                webItem.put("tcccType","REQUEST");
            });
            logger.debug("Received TCCC REQUEST interaction");
        }
    }

    @Override
    public void provideTCCC(boolean local, HlaInteractionManager.HlaProvideTCCCParameters hlaProvideTCCCParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            tcccInteractionStorage.updateFromHla("PROVIDE", webItem -> {
                webItem.put("tcccType","PROVIDE");
                if(hlaProvideTCCCParameters.hasPatient()){
                    webItem.put("patientId",hlaProvideTCCCParameters.getPatient());
                }
            });
            logger.debug("Received TCCC PROVIDE interaction: {}", hlaProvideTCCCParameters);
        }
    }

    @Override
    public void selectScenario(boolean local, HlaInteractionManager.HlaSelectScenarioParameters hlaSelectScenarioParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            scenarioInteractionStorage.updateFromHla("SELECT", webItem -> {
                if(hlaSelectScenarioParameters.hasScenarioName()){
                    webItem.put("scenarioName",hlaSelectScenarioParameters.getScenarioName());
                }
            });
            logger.debug("Received SELECT SCENARIO interaction: {}", hlaSelectScenarioParameters);
        }
    }

    @Override
    public void instructionalStart(boolean local, HlaInteractionManager.HlaInstructionalStartParameters hlaInstructionalStartParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            instructionalInteractionStorage.updateFromHla("START", webItem -> {
                webItem.put("instructionalType","START");
                if(hlaInstructionalStartParameters.hasTrainingFacilityId()){
                    webItem.put("trainingFacilityId",hlaInstructionalStartParameters.getTrainingFacilityId());
                }
            });
            logger.debug("Received INSTRUCTIONAL START interaction: {}", hlaInstructionalStartParameters);
        }
    }

    @Override
    public void instructionalStop(boolean local, HlaInteractionManager.HlaInstructionalStopParameters hlaInstructionalStopParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            instructionalInteractionStorage.updateFromHla("STOP", webItem -> {
                webItem.put("instructionalType","STOP");
                if(hlaInstructionalStopParameters.hasTrainingFacilityId()){
                    webItem.put("trainingFacilityId",hlaInstructionalStopParameters.getTrainingFacilityId());
                }
            });
            logger.debug("Received INSTRUCTIONAL STOP interaction: {}", hlaInstructionalStopParameters);
        }
    }

    @Override
    public void instructionalPause(boolean local, HlaInteractionManager.HlaInstructionalPauseParameters hlaInstructionalPauseParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            instructionalInteractionStorage.updateFromHla("PAUSE", webItem -> {
                webItem.put("instructionalType","PAUSE");
                if(hlaInstructionalPauseParameters.hasTrainingFacilityId()){
                    webItem.put("trainingFacilityId",hlaInstructionalPauseParameters.getTrainingFacilityId());
                }
            });
            logger.debug("Received INSTRUCTIONAL PAUSE interaction: {}", hlaInstructionalPauseParameters);
        }
    }

    @Override
    public void instructionalResume(boolean local, HlaInteractionManager.HlaInstructionalResumeParameters hlaInstructionalResumeParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            instructionalInteractionStorage.updateFromHla("RESUME", webItem -> {
                webItem.put("instructionalType","RESUME");
                if(hlaInstructionalResumeParameters.hasTrainingFacilityId()){
                    webItem.put("trainingFacilityId",hlaInstructionalResumeParameters.getTrainingFacilityId());
                }
            });
            logger.debug("Received INSTRUCTIONAL RESUME interaction: {}", hlaInstructionalResumeParameters);
        }
    }

    @Override
    public void requestLab(boolean local, HlaInteractionManager.HlaRequestLabParameters hlaRequestLabParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            requestLabInteractionStorage.updateFromHla("REQUEST", webItem -> {
                if(hlaRequestLabParameters.hasPatientId()){
                    webItem.put("patientId",hlaRequestLabParameters.getPatientId());
                }
                if(hlaRequestLabParameters.hasLabType()){
                    webItem.put("labType",hlaRequestLabParameters.getLabType());
                }
            });
            logger.debug("Received REQUEST LAB interaction: {}", hlaRequestLabParameters);
        }
    }

    @Override
    public void loadPatient(boolean local, HlaInteractionManager.HlaLoadPatientParameters hlaLoadPatientParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            patientInteractionStorage.updateFromHla("LOAD", webItem -> {
                webItem.put("patientType","LOAD");
                if(hlaLoadPatientParameters.hasPatientId()){
                    webItem.put("patientId",hlaLoadPatientParameters.getPatientId());
                }
            });
            logger.debug("Received LOAD PATIENT interaction: {}", hlaLoadPatientParameters);
        }
    }

    @Override
    public void startPatient(boolean local, HlaInteractionManager.HlaStartPatientParameters hlaStartPatientParameters,
        HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
        patientInteractionStorage.updateFromHla("START", webItem -> {
                webItem.put("patientType","START");
                if(hlaStartPatientParameters.hasPatientId()){
                    webItem.put("patientId",hlaStartPatientParameters.getPatientId());
                }
                if(hlaStartPatientParameters.hasSimulationElapsedTime()){
                    webItem.put("simulationElapsedTime",hlaStartPatientParameters.getSimulationElapsedTime());
                }
            });
        logger.debug("Received START PATIENT interaction: {}", hlaStartPatientParameters);
        }
    }

    @Override
    public void stopPatient(boolean local, HlaInteractionManager.HlaStopPatientParameters hlaStopPatientParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            patientInteractionStorage.updateFromHla("STOP", webItem -> {
                webItem.put("patientType","STOP");
                if(hlaStopPatientParameters.hasPatientId()){
                    webItem.put("patientId",hlaStopPatientParameters.getPatientId());
                }
                if(hlaStopPatientParameters.hasSimulationElapsedTime()){
                    webItem.put("simulationElapsedTime",hlaStopPatientParameters.getSimulationElapsedTime());
                }
            });
            logger.debug("Received STOP PATIENT interaction: {}", hlaStopPatientParameters);
        }
    }

    @Override
    public void pausePatient(boolean local, HlaInteractionManager.HlaPausePatientParameters hlaPausePatientParameters,
        HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
        patientInteractionStorage.updateFromHla("PAUSE", webItem -> {
                webItem.put("patientType","PAUSE");
                if(hlaPausePatientParameters.hasPatientId()){
                    webItem.put("patientId",hlaPausePatientParameters.getPatientId());
                }
                if(hlaPausePatientParameters.hasSimulationElapsedTime()){
                    webItem.put("simulationElapsedTime",hlaPausePatientParameters.getSimulationElapsedTime());
                }
            });
        logger.debug("Received PAUSE PATIENT interaction: {}", hlaPausePatientParameters);
        }
    }

    @Override
    public void resumePatient(boolean local, HlaInteractionManager.HlaResumePatientParameters hlaResumePatientParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local) {
            patientInteractionStorage.updateFromHla("RESUME", webItem -> {
                webItem.put("patientType","RESUME");
                if(hlaResumePatientParameters.hasPatientId()){
                    webItem.put("patientId",hlaResumePatientParameters.getPatientId());
                }
                if(hlaResumePatientParameters.hasSimulationElapsedTime()){
                    webItem.put("simulationElapsedTime",hlaResumePatientParameters.getSimulationElapsedTime());
                }
            });
            logger.debug("Received RESUME PATIENT interaction: {}", hlaResumePatientParameters);
        }
    }

    @Override
    public void magicVitals(boolean local, HlaInteractionManager.HlaMagicVitalsParameters hlaMagicVitalsParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local){
            magicVitalsInteractionStorage.updateFromHla("MAGIC_VITALS",webItem -> {
                if(hlaMagicVitalsParameters.hasPatientId())
                    webItem.put("patientId",hlaMagicVitalsParameters.getPatientId());
                if(hlaMagicVitalsParameters.hasMagicVital())
                    webItem.put("vitalsType",hlaMagicVitalsParameters.getMagicVital());
                if(hlaMagicVitalsParameters.hasMagicVitalValue()){
                    webItem.put("value",hlaMagicVitalsParameters.getMagicVitalValue());
                }
            });
        }
        logger.debug("Received MagicVitals interaction: {}", hlaMagicVitalsParameters);
    }

    @Override
    public void magicTransfer(boolean local, HlaInteractionManager.HlaMagicTransferParameters hlaMagicTransferParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local){
            magicTransferInteractionStorage.updateFromHla(null,webItem -> {
                if(hlaMagicTransferParameters.hasPatientId())
                    webItem.put("patientId",hlaMagicTransferParameters.getPatientId());
                if(hlaMagicTransferParameters.hasFacilityId())
                    webItem.put("facilityId",hlaMagicTransferParameters.getFacilityId());
                webItem.put("receivedAt", ZonedDateTime.now(ZoneOffset.UTC).toString());
            });
        }
        logger.debug("Received MagicTransfer interaction: {}", hlaMagicTransferParameters);
    }

    @Override
    public void vitalsDisplayControl(boolean local, HlaInteractionManager.HlaVitalsDisplayControlParameters hlaVitalsDisplayControlParameters, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!local){
            magicVitalsInteractionStorage.updateFromHla("VITALS_DISPLAY",webItem -> {
                if(hlaVitalsDisplayControlParameters.hasPatientId())
                    webItem.put("patientId",hlaVitalsDisplayControlParameters.getPatientId());
                if(hlaVitalsDisplayControlParameters.hasVisibleVitalSign())
                    webItem.put("visibleVitalSign",hlaVitalsDisplayControlParameters.getVisibleVitalSign());
                if(hlaVitalsDisplayControlParameters.hasToggleVitalSignVisibility()){
                    webItem.put("visibility",hlaVitalsDisplayControlParameters.getToggleVitalSignVisibility());
                }
            });
        }
        logger.debug("Received VitalsDisplayControl interaction: {}", hlaVitalsDisplayControlParameters);
    }

    private void sendControlToHla(WebItem webItem) {
        try {
            switch (webItem.getInstanceName()) {
                case "START":
                    interactionManager.sendStart();
                    break;
                case "STOP":
                    interactionManager.sendStop();
                    break;
                case "PAUSE":
                    interactionManager.sendPause();
                    break;
                case "RESUME":
                    interactionManager.sendResume();
                    break;
                case "SAVE":
                    interactionManager.sendSave((String) webItem.getMap().get("label"));
                    break;
                case "RESTORE":
                    interactionManager.sendRestore((String) webItem.getMap().get("label"));
                    break;
                default:
                    logger.warn("Unknown control interaction: {}", webItem.getInstanceName());
            }
        } catch (Exception e) {
            logger.error("Error processing ControlInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendRestToHla(WebItem webItem){
        try {
            Map<String, Object> valueMap = webItem.getMap();
            switch(webItem.getInstanceName()){
                case "CALL":
                    interactionManager.sendRestCall(
                        (String)valueMap.get("messageId"),
                        (String)valueMap.get("toId"),
                        (String)valueMap.get("fromId"),
                        (String)valueMap.get("callMethod"),
                        (String)valueMap.get("path"),
                        (String)valueMap.get("payload")
                );
                    break;
                case "RESPONSE":
                    interactionManager.sendRestResponse(
                        (String)valueMap.get("messageId"),
                        (String)valueMap.get("toId"),
                        (String)valueMap.get("fromId"),
                        (Integer) valueMap.get("responseCode"),
                        (String)valueMap.get("payload")
                );
                    break;
                default:
                    logger.warn("Unknown REST interaction: {}", webItem.getInstanceName());
            }
        } catch (Exception e) {
            logger.error("Error processing RestInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendMedicalEvacuationToHla(WebItem webItem){
        try {
            Map<String, Object> valueMap = webItem.getMap();
            switch(webItem.getInstanceName()){
                case "REQUEST":
                    interactionManager.sendMedicalEvacuationRequest(
                        (String)valueMap.get("patientId"),
                        toTransportTypeEnum(valueMap.get("transportType")),
                        (String)valueMap.get("siteName")
                );
                    break;
                case "UPDATE":
                    interactionManager.sendMedicalEvacuationUpdate(
                        (String)valueMap.get("patientId"),
                        toMedicalEvacuationStateEnum(valueMap.get("medicalEvacuationState")),
                        (String)valueMap.get("vehicleId"),
                        (String)valueMap.get("siteName")
                );
                    break;
                case "RESPONSE":
                    interactionManager.sendMedicalEvacuationResponse(
                        (String)valueMap.get("patientId"),
                        toMedicalEvacuationStateEnum(valueMap.get("medicalEvacuationState")),
                        (String)valueMap.get("vehicleId"),
                        (String)valueMap.get("siteName")
                );
                    break;
                default:
                    logger.warn("Unknown Medical Evacuation interaction: {}", webItem.getInstanceName());
            }
        } catch (Exception e) {
            logger.error("Error processing MedicalEvacuationInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendTcccToHla(WebItem webItem){
        try {
            switch(webItem.getInstanceName()){
                case "REQUEST":
                    interactionManager.sendRequestTCCC();
                    break;
                case "PROVIDE":
                    interactionManager.sendProvideTCCC((String) webItem.getMap().get("patientId"));
                    break;
                default:
                    logger.warn("Unknown TCCC interaction: {}", webItem.getInstanceName());
            }
        } catch (Exception e) {
            logger.error("Error processing TcccInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendScenarioToHla(WebItem webItem){
        try {
                interactionManager.sendSelectScenario((String)webItem.getMap().get("scenarioName"));
        } catch (Exception e) {
            logger.error("Error processing ScenarioInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendInstructionalToHla(WebItem webItem){
        try {
            switch(webItem.getInstanceName()){
                case "START":
                    interactionManager.sendInstructionalStart((String) webItem.getMap().get("trainingFacilityId"));
                    break;
                case "STOP":
                    interactionManager.sendInstructionalStop((String) webItem.getMap().get("trainingFacilityId"));
                    break;
                case "PAUSE":
                    interactionManager.sendInstructionalPause((String) webItem.getMap().get("trainingFacilityId"));
                    break;
                case "RESUME":
                    interactionManager.sendInstructionalResume((String) webItem.getMap().get("trainingFacilityId"));
                    break;
                default:
                    logger.warn("Unknown Instructional interaction: {}", webItem.getInstanceName());
            }
        } catch (Exception e) {
            logger.error("Error processing InstructionalInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendRequestLabToHla(WebItem webItem){
        try {
            interactionManager.sendRequestLab(
                    (String)webItem.getMap().get("patientId"),
                    (String)webItem.getMap().get("labType"));
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaFomException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendPatientToHla(WebItem webItem) {
        try {
            Map<String, Object> valueMap = webItem.getMap();
            switch (webItem.getInstanceName()) {
                case "LOAD":
                    interactionManager.sendLoadPatient((String) valueMap.get("patientId"));
                    break;
                case "START":
                    interactionManager.sendStartPatient(
                            (Long) valueMap.get("simulationElapsedTime"),
                            (String) valueMap.get("patientId")
                    );
                    break;
                case "STOP":
                    interactionManager.sendStopPatient(
                            (Long) valueMap.get("simulationElapsedTime"),
                            (String) valueMap.get("patientId")
                    );
                    break;
                case "PAUSE":
                    interactionManager.sendPausePatient(
                            (Long) valueMap.get("simulationElapsedTime"),
                            (String) valueMap.get("patientId")
                    );
                    break;
                case "RESUME":
                    interactionManager.sendResumePatient(
                            (Long) valueMap.get("simulationElapsedTime"),
                            (String) valueMap.get("patientId")
                    );
                    break;
                default:
                    logger.warn("Unknown patient interaction: {}", webItem.getInstanceName());
            }
        } catch (Exception e) {
            logger.error("Error processing Patient interaction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendMagicVitalsToHla(WebItem webItem){
        try {
            interactionManager.sendMagicVitals(
                    (String)webItem.getMap().get("patientId"),
                    MagicVitalsEnum.valueOf(webItem.getMap().get("vitalsType").toString()),
                    (Float)webItem.getMap().get("value")
            );
        } catch (Exception e) {
            logger.error("Error processing MagicVitalsInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendMagicTransferToHla(WebItem webItem){
        try{
            interactionManager.sendMagicTransfer(
                    (String)webItem.getMap().get("patientId"),
                    (String)webItem.getMap().get("facilityId")
            );
        } catch (Exception e){
            logger.error("Error processing MagicTransferInteraction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void sendVitalsDisplayControlToHla(WebItem webItem){
        try {
            interactionManager.sendVitalsDisplayControl(
                    (String)webItem.getMap().get("patientId"),
                    (Boolean)webItem.getMap().get("visibility"),
                    VisibleVitalSignEnum.valueOf(webItem.getMap().get("visibleVitalSign").toString())
            );
        } catch (Exception e) {
            logger.error("Error processing VitalsDisplayControl Interaction: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private TransportTypeEnum toTransportTypeEnum(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return TransportTypeEnum.valueOf(value.toString());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid TransportTypeEnum value: {}", value, e);
            return null;
        }
    }


    private MedicalEvacuationStateEnum toMedicalEvacuationStateEnum(Object value){
        if(value == null){
            return null;
        }
        try {
        return MedicalEvacuationStateEnum.valueOf(value.toString());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid MedicalEvacuationStateEnum value: {}", value, e);
            return null;
        }
    }

    private class InteractionWebItemToHlaCallback implements WebItemToHlaCallback{
        private Consumer<WebItem> _sendToHla;

        public InteractionWebItemToHlaCallback(Consumer<WebItem> sendToHla){
            _sendToHla = sendToHla;
        }

        @Override
        public void sendToHla(WebItem webItem) {
            _sendToHla.accept(webItem);
        }

        @Override
        public String sendNewItemToHla(WebItem webItem) {
            _sendToHla.accept(webItem);
            return "done";
        }
    }

}
