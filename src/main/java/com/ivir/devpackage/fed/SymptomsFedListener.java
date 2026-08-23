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

import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import com.ivir.devpackage.model.Storage;
import devstudio.generatedcode.*;
import devstudio.generatedcode.datatypes.*;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SymptomsFedListener implements HlaSymptomsListener {
    private HlaSymptomsManager symptomsManager;
    private Storage storage;

    public SymptomsFedListener(Storage storage, HlaSymptomsManager symptomsManager) {
        this.storage = storage;
        this.symptomsManager = symptomsManager;
        this.storage.setWebItemToHlaCallback(new WebItemToHlaCallback() {
            @Override
            public void sendToHla(WebItem webItem) {
                SymptomsFedListener.this.sendToHla(webItem);
            }

            @Override
            public String sendNewItemToHla(WebItem webItem) {
                return SymptomsFedListener.this.sendNewItemToHla(webItem);
            }
        });
    }

    @Override
    public void attributesUpdated(HlaSymptoms hlaSymptoms, Set<HlaSymptomsAttributes.Attribute> set, HlaTimeStamp timeStamp, HlaLogicalTime logicalTime) {
        if (!hlaSymptoms.isLocal()) {
            storage.updateFromHla(hlaSymptoms.getHlaInstanceName(), (webItem) -> updateWebItem(hlaSymptoms, set, webItem));
        }
    }

    private void updateWebItem(HlaSymptoms hlaSymptoms, Set<HlaSymptomsAttributes.Attribute> set, WebItem webItem) {
        for (HlaSymptomsAttributes.Attribute attribute : set) {
            switch (attribute) {
                case DIZZINESS -> webItem.put(attribute.getName(), hlaSymptoms.getDizziness());
                case FATIGUE -> webItem.put(attribute.getName(), hlaSymptoms.getFatigue());
                case LEVEL_OF_PAIN -> webItem.put(attribute.getName(), hlaSymptoms.getLevelOfPain());
                case NAUSEA -> webItem.put(attribute.getName(), hlaSymptoms.getNausea());
                case NUMBNESS -> webItem.put(attribute.getName(), hlaSymptoms.getNumbness());
                case PATIENT_ID -> webItem.put(attribute.getName(), hlaSymptoms.getPatientId());
                case SYMPTOM_LOCATION -> webItem.put(attribute.getName(), BodyLocationMapper.toSerializableObject(hlaSymptoms.getSymptomLocation()));
                case VISION_DISTURBANCE -> webItem.put(attribute.getName(), hlaSymptoms.getVisionDisturbance().toString());
            }
        }
    }

    private void sendToHla(WebItem webItem) {
        HlaSymptoms symptoms = this.symptomsManager.getSymptomsByHlaInstanceName(webItem.getInstanceName());
        HlaSymptomsUpdater updater = symptoms.getHlaSymptomsUpdater();
        updateAndSend(webItem, updater);
    }

    private String sendNewItemToHla(WebItem webItem) {
        try {
            HlaSymptoms symptoms = this.symptomsManager.createLocalHlaSymptoms();
            HlaSymptomsUpdater updater = symptoms.getHlaSymptomsUpdater();
            updateAndSend(webItem, updater);
            return symptoms.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaSymptomsUpdater updater) {
        webItem.getMap().forEach((key, value) -> {
            HlaSymptomsAttributes.Attribute attribute = HlaSymptomsAttributes.Attribute.find(key);
            if (attribute != null && value != null) {
                try {
                    switch (attribute) {
                        case DIZZINESS -> updater.setDizziness(Boolean.parseBoolean(value.toString()));
                        case FATIGUE ->  updater.setFatigue(Boolean.parseBoolean(value.toString()));
                        case LEVEL_OF_PAIN -> updater.setLevelOfPain(Integer.parseInt(value.toString()));
                        case NAUSEA -> updater.setNausea(Boolean.parseBoolean(value.toString()));
                        case NUMBNESS -> updater.setNumbness(Boolean.parseBoolean(value.toString()));
                        case PATIENT_ID -> updater.setPatientId(value.toString());
                        case SYMPTOM_LOCATION -> updater.setSymptomLocation(BodyLocationMapper.toBodyLocationRecord(value));
                        case VISION_DISTURBANCE -> updater.setVisionDisturbance(VisionDisturbanceEnum.valueOf(value.toString()));
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid enum value for attribute " + attribute.getName() + ": " + value);
                }
            }
        });

        try {
            updater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }
}
