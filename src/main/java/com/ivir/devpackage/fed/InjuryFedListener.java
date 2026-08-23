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
import static com.ivir.devpackage.fed.FedListenerUtility.toFloat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class InjuryFedListener implements HlaInjuryListener, WebItemToHlaCallback {
    private final HlaInjuryManager injuryManager;
    private final Storage injuryStorage;

    public InjuryFedListener(Storage storage, HlaInjuryManager injuryManager){
        if (storage == null || injuryManager == null) {
            throw new IllegalArgumentException("Storage and InjuryManager cannot be null.");
        }
        this.injuryStorage = storage;
        this.injuryManager = injuryManager;
        init();
    }

    private void init() {
        this.injuryStorage.setWebItemToHlaCallback(this); // Safe because the object is fully constructed now
    }

    @Override
    public void attributesUpdated(HlaInjury hlaInjury, Set<HlaInjuryAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaInjury.isLocal()) {
            injuryStorage.updateFromHla(hlaInjury.getHlaInstanceName(), (webItem) -> updateWebItem(hlaInjury, set, webItem));
        }
    }

    private void updateWebItem(HlaInjury hlaInjury, Set<HlaInjuryAttributes.Attribute> set, WebItem webItem){
        for(HlaInjuryAttributes.Attribute att : set){
            if (att == null) {
                logWarning("Received null attribute in updateWebItem. Skipping.");
                continue;
            }
            switch (att){
                case INJURY_ID -> webItem.put(att.getName(), safeString(hlaInjury.getInjuryId()));
                case INJURY_DESCRIPTION -> webItem.put(att.getName(), safeEnumValue(hlaInjury.getInjuryDescription()));
                case INJURY_LOCATION -> webItem.put(att.getName(),
                        BodyLocationMapper.toSerializableObject(hlaInjury.getInjuryLocation()));
                case INJURY_SEVERITY -> webItem.put(att.getName(),hlaInjury.getInjurySeverity());
                case INJURY_TYPE -> webItem.put(att.getName(), safeEnumValue(hlaInjury.getInjuryType()));
                case TOTAL_BODY_SURFACE_AREA -> webItem.put(att.getName(), hlaInjury.getTotalBodySurfaceArea());
                case INJURY_DETAIL -> webItem.put(att.getName(), safeString(hlaInjury.getInjuryDetail()));
                case MECHANISM_OF_INJURY -> webItem.put(att.getName(), toSerializableObject(hlaInjury.getMechanismOfInjury()));
                case HEMORRHAGE_RATE -> webItem.put(att.getName(), hlaInjury.getHemorrhageRate());
                case PATIENT_ID -> webItem.put(att.getName(), safeString(hlaInjury.getPatientId()));
                case TIME -> webItem.put(att.getName(),hlaInjury.getTime());
                default -> logWarning("Unhandled attribute: " + att.getName());
            }
        }
    }

    private Map<String, Object> toSerializableObject(MechanismOfInjuryRecord record){
        HashMap<String, Object> serializableMap = new HashMap<>();
        serializableMap.put("blade", record.getBlade());
        serializableMap.put("blast", record.getBlast());
        serializableMap.put("cbrn", record.getCbrn());
        serializableMap.put("fall", record.getFall());
        serializableMap.put("gunshotAmmunitionType", record.getGunshotAmmunitionType());
        serializableMap.put("gunshotCaliber", record.getGunshotCaliber());
        serializableMap.put("shrapnel", record.getShrapnel());
        serializableMap.put("vehicleCrash", record.getVehicleCrash());
        return serializableMap;
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaInjury injury = this.injuryManager.getInjuryByHlaInstanceName(webItem.getInstanceName());
        HlaInjuryUpdater injuryUpdater = injury.getHlaInjuryUpdater();
        updateAndSend(webItem, injuryUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaInjury injury = this.injuryManager.createLocalHlaInjury();
            HlaInjuryUpdater injuryUpdater = injury.getHlaInjuryUpdater();
            updateAndSend(webItem, injuryUpdater);
            return injury.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaInjuryUpdater injuryUpdater){
        webItem.getMap().forEach((key,value)->{
            if (key == null || value == null) {
                logWarning("Received null key or value in webItem map. Skipping.");
                return;
            }
            HlaInjuryAttributes.Attribute att = HlaInjuryAttributes.Attribute.find(key);
            if (att == null) {
                logWarning("Attribute key " + key + " not found. Skipping.");
                return;
            }
            try {
                switch (att){
                    case INJURY_ID -> injuryUpdater.setInjuryId((String)value);
                    case INJURY_DESCRIPTION -> injuryUpdater.setInjuryDescription(toInjuryDescriptionEnum(value));
                    case INJURY_LOCATION -> injuryUpdater.setInjuryLocation(BodyLocationMapper.toBodyLocationRecord(value));
                    case INJURY_SEVERITY -> injuryUpdater.setInjurySeverity((int)value);
                    case INJURY_TYPE -> injuryUpdater.setInjuryType(toInjuryTypeEnum(value));
                    case TOTAL_BODY_SURFACE_AREA -> injuryUpdater.setTotalBodySurfaceArea(toFloat(value));
                    case INJURY_DETAIL -> injuryUpdater.setInjuryDetail((String) value);
                    case MECHANISM_OF_INJURY -> injuryUpdater.setMechanismOfInjury(toMechanismOfInjuryRecord(value));
                    case HEMORRHAGE_RATE -> injuryUpdater.setHemorrhageRate(toFloat(value));
                    case PATIENT_ID -> injuryUpdater.setPatientId((String)value);
                    case TIME -> injuryUpdater.setTime((long)value);
                    default -> logWarning("Unhandled attribute key: " + key);
                }
            } catch (Exception e) {
                logWarning("Error processing attribute " + att + " with value " + value + ". Error: " + e.getMessage());
            }
        });

        try {
            injuryUpdater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private InjuryDescriptionEnum toInjuryDescriptionEnum(Object value) {
        if(value == null){
            return InjuryDescriptionEnum.NOT_APPLICABLE;
        }
        try {
            return InjuryDescriptionEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            logWarning("V2 injury description " + value + " does not match v3 InjuryDescriptionEnum. Defaulting to NOT_APPLICABLE.");
            return InjuryDescriptionEnum.NOT_APPLICABLE;
        }
    }

    private InjuryTypeEnum toInjuryTypeEnum(Object value){
        if(value == null){
            return InjuryTypeEnum.NOT_APPLICABLE;
        }
        try {
            return InjuryTypeEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            logWarning("V2 injury type " + value + " does not match v3 InjuryTypeEnum. Defaulting to NOT_APPLICABLE.");
            return InjuryTypeEnum.NOT_APPLICABLE;
        }
    }

    private MechanismOfInjuryRecord toMechanismOfInjuryRecord(Object value){
        if(value == null){
            return null;
        }
        Map<String, Object> valueMap = (Map<String, Object>) value;
        return MechanismOfInjuryRecord.create(
                toEnumValue(valueMap.get("gunshotCaliber"), GunshotCaliberEnum.NOT_APPLICABLE,GunshotCaliberEnum::valueOf),
                toEnumValue(valueMap.get("gunshotAmmunitionTypeEnu"), GunshotAmmunitionTypeEnum.NOT_APPLICABLE,GunshotAmmunitionTypeEnum::valueOf),
                toEnumValue(valueMap.get("blade"), BladeTypeEnum.NOT_APPLICABLE, BladeTypeEnum::valueOf),
                toEnumValue(valueMap.get("blast"), BlastTypeEnum.NOT_APPLICABLE, BlastTypeEnum::valueOf),
                toEnumValue(valueMap.get("vehicleCrash"), VehicleCrashEnum.NOT_APPLICABLE, VehicleCrashEnum::valueOf),
                toEnumValue(valueMap.get("fall"), FallTypeEnum.NOT_APPLICABLE, FallTypeEnum::valueOf),
                toEnumValue(valueMap.get("cbrn"), CBRNTypeEnum.NOT_APPLICABLE, CBRNTypeEnum::valueOf),
                toEnumValue(valueMap.get("shrapnel"), ShrapnelTypeEnum.NOT_APPLICABLE, ShrapnelTypeEnum::valueOf)
        );
    }

    private <T> T toEnumValue(Object value, T enumDefault, Function<String,T> convert){
        if(value == null){
            return enumDefault;
        }
        return convert.apply(value.toString());
    }

    private void logWarning(String message) {
        System.err.println("Warning: " + message);
        }

    private String safeString(String value) {
        return value == null ? "" : value;
    }

    private <T extends Enum<T>> String safeEnumValue(T enumValue) {
        return enumValue == null ? "NOT_APPLICABLE" : enumValue.name();
    }
}
