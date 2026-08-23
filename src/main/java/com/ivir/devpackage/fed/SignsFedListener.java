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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SignsFedListener implements HlaSignsListener {
    private HlaSignsManager signsManager;
    private Storage storage;

    public SignsFedListener(Storage storage, HlaSignsManager signsManager) {
        this.storage = storage;
        this.signsManager = signsManager;
        this.storage.setWebItemToHlaCallback(new WebItemToHlaCallback() {
            @Override
            public void sendToHla(WebItem webItem) {
                SignsFedListener.this.sendToHla(webItem);
            }

            @Override
            public String sendNewItemToHla(WebItem webItem) {
                return SignsFedListener.this.sendNewItemToHla(webItem);
            }
        });
    }

    @Override
    public void attributesUpdated(HlaSigns hlaSigns, Set<HlaSignsAttributes.Attribute> set, HlaTimeStamp timeStamp, HlaLogicalTime logicalTime) {
        if (!hlaSigns.isLocal()) {
            storage.updateFromHla(hlaSigns.getHlaInstanceName(), (webItem) -> updateWebItem(hlaSigns, set, webItem));
        }
    }

    private void updateWebItem(HlaSigns hlaSigns, Set<HlaSignsAttributes.Attribute> set, WebItem webItem) {
        for (HlaSignsAttributes.Attribute attribute : set) {
            switch (attribute) {
                case BOWEL_SOUND -> webItem.put(attribute.getName(), hlaSigns.getBowelSound().name());
                case CONFUSION -> webItem.put(attribute.getName(), hlaSigns.getConfusion());
                case COUGH -> webItem.put(attribute.getName(), hlaSigns.getCough().name());
                case ECG_RHYTHM -> webItem.put(attribute.getName(), hlaSigns.getEcgRhythm().name());
                case HEART_SOUND -> webItem.put(attribute.getName(), hlaSigns.getHeartSound().name());
                case LUNG_SOUND -> webItem.put(attribute.getName(), hlaSigns.getLungSound().name());
                case PATIENT_ID -> webItem.put(attribute.getName(), hlaSigns.getPatientId());
                case PUPIL_SIZE -> webItem.put(attribute.getName(), hlaSigns.getPupilSize().name());
                case SIGN_LOCATION -> webItem.put(attribute.getName(), BodyLocationMapper.toSerializableObject(hlaSigns.getSignLocation()));
                case SKIN_COLOR -> webItem.put(attribute.getName(), hlaSigns.getSkinColor().name());
                case SKIN_MOISTURE -> webItem.put(attribute.getName(), hlaSigns.getSkinMoisture());
                case SKIN_RASH -> webItem.put(attribute.getName(), toMap(hlaSigns.getSkinRash()));
                case SHIVERING -> webItem.put(attribute.getName(), hlaSigns.getShivering());
            }
        }
    }

    private void sendToHla(WebItem webItem) {
        HlaSigns signs = this.signsManager.getSignsByHlaInstanceName(webItem.getInstanceName());
        HlaSignsUpdater updater = signs.getHlaSignsUpdater();
        updateAndSend(webItem, updater);
    }

    private String sendNewItemToHla(WebItem webItem) {
        try {
            HlaSigns signs = this.signsManager.createLocalHlaSigns();
            HlaSignsUpdater updater = signs.getHlaSignsUpdater();
            updateAndSend(webItem, updater);
            return signs.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaSignsUpdater updater) {
        webItem.getMap().forEach((key, value) -> {
            HlaSignsAttributes.Attribute attribute = HlaSignsAttributes.Attribute.find(key);
            if (attribute != null && value != null) {
                try {
                    switch (attribute) {
                        case BOWEL_SOUND -> updater.setBowelSound(BowelSoundEnum.valueOf(value.toString()));
                        case CONFUSION -> updater.setConfusion(Boolean.parseBoolean(value.toString()));
                        case COUGH -> updater.setCough(CoughEnum.valueOf(value.toString()));
                        case ECG_RHYTHM -> updater.setEcgRhythm(EcgRhythmEnum.valueOf(value.toString()));
                        case PATIENT_ID -> updater.setPatientId(value.toString());
                        case HEART_SOUND -> updater.setHeartSound(HeartSoundEnum.valueOf(value.toString()));
                        case LUNG_SOUND -> updater.setLungSound(LungSoundEnum.valueOf(value.toString()));
                        case PUPIL_SIZE -> updater.setPupilSize(PupilSizeEnum.valueOf(value.toString()));
                        case SIGN_LOCATION -> updater.setSignLocation(BodyLocationMapper.toBodyLocationRecord(value));
                        case SKIN_COLOR -> updater.setSkinColor(SkinColorEnum.valueOf(value.toString()));
                        case SKIN_MOISTURE -> updater.setSkinMoisture(Boolean.valueOf(value.toString()));
                        case SKIN_RASH -> updater.setSkinRash(toSkinRashRecord(value));
                        case SHIVERING -> updater.setShivering(Boolean.parseBoolean(value.toString()));
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

    private SkinRashRecord toSkinRashRecord(Object value){
        Map<String, Object> map = (Map<String, Object>)value;
        return SkinRashRecord.create(
                Boolean.valueOf(map.get("rashRaised").toString()),
                Boolean.valueOf(map.get("rashUniform").toString()),
                Boolean.valueOf(map.get("rashScab").toString())
                );
    }

    private Map<String, Object> toMap(SkinRashRecord record){
        HashMap<String, Object> map = new HashMap<>();
        map.put("rashRaised",record.getRashRaised());
        map.put("rashUniform",record.getRashUniform());
        map.put("rashScab",record.getRashScab());
        return map;
    }
}
