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

public class PhysicalTreatmentFedListener implements HlaPhysicalTreatmentListener, WebItemToHlaCallback {
    private HlaPhysicalTreatmentManager physicalTreatmentManager;

    private Storage physicalTreatmentStorage;

    public PhysicalTreatmentFedListener(Storage storage, HlaPhysicalTreatmentManager physicalTreatmentManager){
        this.physicalTreatmentStorage = storage;
        this.physicalTreatmentManager = physicalTreatmentManager;
        this.physicalTreatmentStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaPhysicalTreatment hlaPhysicalTreatment, Set<HlaPhysicalTreatmentAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaPhysicalTreatment.isLocal()) {
            physicalTreatmentStorage.updateFromHla(hlaPhysicalTreatment.getHlaInstanceName(),
                    (webItem) -> updateWebItem(hlaPhysicalTreatment, set, webItem));
        }

    }

    private void updateWebItem(HlaPhysicalTreatment hlaPhysicalTreatment, Set<HlaPhysicalTreatmentAttributes.Attribute> set, WebItem webItem){
        for(HlaPhysicalTreatmentAttributes.Attribute att : set){
            switch (att){
                case TREATMENT_ID -> webItem.put(att.getName(),hlaPhysicalTreatment.getTreatmentId());
                case TREATMENT -> webItem.put(att.getName(),hlaPhysicalTreatment.getTreatment());
                case TREATMENT_ACTIVE -> webItem.put(att.getName(),hlaPhysicalTreatment.getTreatmentActive());
                case DEVICE_USED -> webItem.put(att.getName(),hlaPhysicalTreatment.getDeviceUsed());
                case TREATMENT_LOCATION -> webItem.put(att.getName(),
                        BodyLocationMapper.toSerializableObject(hlaPhysicalTreatment.getTreatmentLocation()));
                case TREATMENT_TIME -> webItem.put(att.getName(),hlaPhysicalTreatment.getTreatmentTime());
                case INJURY_ID -> webItem.put(att.getName(),hlaPhysicalTreatment.getInjuryId());
                case PATIENT_ID -> webItem.put(att.getName(),hlaPhysicalTreatment.getPatientId());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaPhysicalTreatment physicalTreatment = this.physicalTreatmentManager.getPhysicalTreatmentByHlaInstanceName(webItem.getInstanceName());
        HlaPhysicalTreatmentUpdater physicalTreatmentUpdater = physicalTreatment.getHlaPhysicalTreatmentUpdater();
        updateAndSend(webItem, physicalTreatmentUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaPhysicalTreatment physicalTreatment = this.physicalTreatmentManager.createLocalHlaPhysicalTreatment();
            HlaPhysicalTreatmentUpdater physicalTreatmentUpdater = physicalTreatment.getHlaPhysicalTreatmentUpdater();
            updateAndSend(webItem, physicalTreatmentUpdater);
            return physicalTreatment.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaPhysicalTreatmentUpdater physicalTreatmentUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaPhysicalTreatmentAttributes.Attribute att = HlaPhysicalTreatmentAttributes.Attribute.find(key);
            if (att != null && value != null) {
                switch (att){
                    case TREATMENT_ID -> physicalTreatmentUpdater.setTreatmentId((String)value);
                    case TREATMENT -> physicalTreatmentUpdater.setTreatment(toPhysicalTreatmentTypeEnum(value));
                    case INJURY_ID -> physicalTreatmentUpdater.setInjuryId((String)value);
                    case TREATMENT_LOCATION -> physicalTreatmentUpdater.setTreatmentLocation(BodyLocationMapper.toBodyLocationRecord(value));
                    case TREATMENT_TIME -> physicalTreatmentUpdater.setTreatmentTime((long)value);
                    case PATIENT_ID -> physicalTreatmentUpdater.setPatientId((String)value);
                    case DEVICE_USED -> physicalTreatmentUpdater.setDeviceUsed(toTreatmentDeviceEnum(value));
                    case TREATMENT_ACTIVE -> physicalTreatmentUpdater.setTreatmentActive((boolean)value);
                }
            }
        });
        try {
            physicalTreatmentUpdater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
    }
    }

    private PhysicalTreatmentTypeEnum toPhysicalTreatmentTypeEnum(Object value){
        if(value == null){
            return null;
        }
        return PhysicalTreatmentTypeEnum.valueOf(value.toString());
    }

    private TreatmentDeviceEnum toTreatmentDeviceEnum(Object value){
        if(value == null){
            return null;
        }
        return TreatmentDeviceEnum.valueOf(value.toString());
    }

}
