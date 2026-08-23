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
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import devstudio.generatedcode.*;
import devstudio.generatedcode.datatypes.*;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

public class MedicationTreatmentFedListener implements HlaMedicationTreatmentListener, WebItemToHlaCallback {
    private HlaMedicationTreatmentManager medicationTreatmentManager;

    private Storage medicationTreatmentStorage;

    public MedicationTreatmentFedListener(Storage storage, HlaMedicationTreatmentManager medicationTreatmentManager){
        this.medicationTreatmentStorage = storage;
        this.medicationTreatmentManager = medicationTreatmentManager;
        this.medicationTreatmentStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaMedicationTreatment hlaMedicationTreatment, Set<HlaMedicationTreatmentAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaMedicationTreatment.isLocal()) {
            medicationTreatmentStorage.updateFromHla(hlaMedicationTreatment.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaMedicationTreatment, set, webItem);
            });
        }
    }

    private void updateWebItem(HlaMedicationTreatment hlaMedicationTreatment, Set<HlaMedicationTreatmentAttributes.Attribute> set, WebItem webItem){
        for(HlaMedicationTreatmentAttributes.Attribute att : set){
            switch (att){
                case TREATMENT_ID -> webItem.put(att.getName(), safeString(hlaMedicationTreatment.getTreatmentId()));
                case MEDICATION -> webItem.put(att.getName(), safeEnumValue(hlaMedicationTreatment.getMedication()));
                case ADMINISTRATION_ROUTE -> webItem.put(att.getName(), safeEnumValue(hlaMedicationTreatment.getAdministrationRoute()));
                case DOSAGE_ACTIVE -> webItem.put(att.getName(),hlaMedicationTreatment.getDosageActive());
                case DOSAGE_TIME_PERIOD -> webItem.put(att.getName(), hlaMedicationTreatment.getDosageTimePeriod());
                case DOSAGE_VALUE -> webItem.put(att.getName(), hlaMedicationTreatment.getDosageValue());
                case TREATMENT_LOCATION -> webItem.put(att.getName(),
                        BodyLocationMapper.toSerializableObject(hlaMedicationTreatment.getTreatmentLocation()));
                case TREATMENT_TIME -> webItem.put(att.getName(),hlaMedicationTreatment.getTreatmentTime());
                case INJURY_ID -> webItem.put(att.getName(), safeString(hlaMedicationTreatment.getInjuryId()));
                case PATIENT_ID -> webItem.put(att.getName(), safeString(hlaMedicationTreatment.getPatientId()));
                default -> logWarning("Unhandled attribute: " + att.getName());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaMedicationTreatment medicationTreatment = this.medicationTreatmentManager.getMedicationTreatmentByHlaInstanceName(webItem.getInstanceName());
        HlaMedicationTreatmentUpdater medicationTreatmentUpdater = medicationTreatment.getHlaMedicationTreatmentUpdater();
        updateAndSend(webItem, medicationTreatmentUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaMedicationTreatment medicationTreatment = this.medicationTreatmentManager.createLocalHlaMedicationTreatment();
            HlaMedicationTreatmentUpdater medicationTreatmentUpdater = medicationTreatment.getHlaMedicationTreatmentUpdater();
            updateAndSend(webItem, medicationTreatmentUpdater);
            return medicationTreatment.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaMedicationTreatmentUpdater medicationTreatmentUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaMedicationTreatmentAttributes.Attribute att = HlaMedicationTreatmentAttributes.Attribute.find(key);
            if (att != null && value != null) {
                try {
                switch (att){
                        case TREATMENT_ID -> medicationTreatmentUpdater.setTreatmentId((String) value);
                        case MEDICATION -> medicationTreatmentUpdater.setMedication(toMedicationEnum(value));
                        case INJURY_ID -> medicationTreatmentUpdater.setInjuryId((String) value);
                        case TREATMENT_LOCATION -> medicationTreatmentUpdater.setTreatmentLocation(BodyLocationMapper.toBodyLocationRecord(value));
                        case TREATMENT_TIME -> medicationTreatmentUpdater.setTreatmentTime((long) value);
                        case PATIENT_ID -> medicationTreatmentUpdater.setPatientId((String) value);
                    case ADMINISTRATION_ROUTE -> medicationTreatmentUpdater.setAdministrationRoute(toMedicationAdministrationRouteEnum(value));
                        case DOSAGE_ACTIVE -> medicationTreatmentUpdater.setDosageActive((boolean) value);
                        case DOSAGE_TIME_PERIOD -> medicationTreatmentUpdater.setDosageTimePeriod((int) value);
                    case DOSAGE_VALUE -> medicationTreatmentUpdater.setDosageValue(FedListenerUtility.toFloat(value));
                        default -> logWarning("Unhandled attribute key: " + key);
                    }
                } catch (Exception e) {
                    logWarning("Error processing attribute " + att + " with value " + value + ". Error: " + e.getMessage());
                }
            }else{
                logWarning("Attribute key " + key + " is null or value is null. Skipping.");
            }
        });
        try {
            medicationTreatmentUpdater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private MedicationEnum toMedicationEnum(Object value) {
        if(value == null){
            return MedicationEnum.NOT_APPLICABLE;
        }
        try {
            return MedicationEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            logWarning("Invalid MedicationEnum value: " + value + ". Defaulting to NOT_APPLICABLE.");
            return MedicationEnum.NOT_APPLICABLE;
        }
    }

    private MedicationAdministrationRouteEnum toMedicationAdministrationRouteEnum(Object value){
        if(value == null){
            return MedicationAdministrationRouteEnum.UNKNOWN;
        }
        try {
            return MedicationAdministrationRouteEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            logWarning("Invalid MedicationAdministrationRouteEnum value: " + value + ". Defaulting to UNKNOWN.");
            return MedicationAdministrationRouteEnum.UNKNOWN;
    }
        }

    private void logWarning(String message) {
        System.err.println("Warning: " + message);
        // Ideally, use a proper logging framework for production use
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }

    private <T extends Enum<T>> String safeEnumValue(T enumValue) {
        return enumValue == null ? "NOT_APPLICABLE" : enumValue.name();
    }
}
