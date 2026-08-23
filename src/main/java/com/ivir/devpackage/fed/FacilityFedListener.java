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
import devstudio.generatedcode.datatypes.FacilityTypeEnum;
import devstudio.generatedcode.datatypes.LearnerActionEnum;
import devstudio.generatedcode.datatypes.PhysicalLocationRecord;
import devstudio.generatedcode.datatypes.RoleOfCareEnum;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import static com.ivir.devpackage.fed.FedListenerUtility.toDouble;
import static com.ivir.devpackage.fed.FedListenerUtility.toWebItemValueWithNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FacilityFedListener implements HlaFacilityListener, WebItemToHlaCallback {
    private HlaFacilityManager facilityManager;

    private Storage facilityStorage;

    public FacilityFedListener(Storage storage, HlaFacilityManager facilityManager){
        this.facilityStorage = storage;
        this.facilityManager = facilityManager;
        this.facilityStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaFacility hlaFacility, Set<HlaFacilityAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaFacility.isLocal()) {
            facilityStorage.updateFromHla(hlaFacility.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaFacility, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaFacility hlaFacility, Set<HlaFacilityAttributes.Attribute> set, WebItem webItem){
        for(HlaFacilityAttributes.Attribute att : set){
            Object value = null;
            switch (att){
                case FACILITY_ID -> value = hlaFacility.getFacilityId();
                case FACILITY_TYPE -> value = toWebItemValueWithNull(hlaFacility.getFacilityType());
                case LOCATION -> value = toWebItemValue(hlaFacility.getLocation());
                case PATIENT_CAPACITY -> value = hlaFacility.getPatientCapacity();
                case ROLE_OF_CARE -> value = toWebItemValueWithNull(hlaFacility.getRoleOfCare());
            }

            if (value != null) {
                webItem.put(att.getName(), value); // Only update if value is not null
            }
        }
    }

    private Map<String,Object> toWebItemValue(PhysicalLocationRecord record){
        if(record == null){
            return null;
        }
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("altitude", record.getAltitude());
        outMap.put("latitude", record.getLatitude());
        outMap.put("longitude", record.getLongitude());
        return outMap;
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaFacility facility = this.facilityManager.getFacilityByHlaInstanceName(webItem.getInstanceName());
        HlaFacilityUpdater facilityUpdater = facility.getHlaFacilityUpdater();
        updateAndSend(webItem, facilityUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaFacility facility = this.facilityManager.createLocalHlaFacility();
            HlaFacilityUpdater facilityUpdater = facility.getHlaFacilityUpdater();
            updateAndSend(webItem, facilityUpdater);
            return facility.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaFacilityUpdater facilityUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaFacilityAttributes.Attribute att = HlaFacilityAttributes.Attribute.find(key);

            if (att != null && value != null) { // Skip null attributes or values
                switch (att){
                    case LOCATION -> facilityUpdater.setLocation(toPhysicalLocationRecord(value));
                    case FACILITY_ID -> facilityUpdater.setFacilityId((String)value);
                    case FACILITY_TYPE -> facilityUpdater.setFacilityType(toFacilityTypeEnum(value));
                    case PATIENT_CAPACITY -> facilityUpdater.setPatientCapacity((int)value);
                    case ROLE_OF_CARE -> facilityUpdater.setRoleOfCare(toRoleOfCareEnum(value));
                }
            }
        });

        try {
            facilityUpdater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private PhysicalLocationRecord toPhysicalLocationRecord(Object value){
        Map<String, Object> map = (Map<String, Object>) value;
        return PhysicalLocationRecord.create(
                toDouble(map.get("latitude")),
                toDouble(map.get("longitude")),
                toDouble(map.get("altitude")));
    }

    private FacilityTypeEnum toFacilityTypeEnum(Object value){
        if (value == null) return null; // Null indicates no update has been made
        try {
            return FacilityTypeEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the value cannot be mapped
        }
    }

    private RoleOfCareEnum toRoleOfCareEnum(Object value){
        if(value == null) return null;
        try{
            return RoleOfCareEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the value cannot be mapped
        }
    }
}
