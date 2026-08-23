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

import com.ivir.devpackage.api.model.OwnershipToHlaCallback;
import com.ivir.devpackage.model.Storage;
import com.ivir.devpackage.model.StorageService;
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import com.ivir.devpackage.model.enums.OwnershipStateEnum;
import devstudio.generatedcode.*;
import devstudio.generatedcode.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class VitalSignsFedListener implements HlaVitalSignsListener, WebItemToHlaCallback, OwnershipToHlaCallback, HlaVitalSignsOwnershipListener{
    private static final Logger logger = LoggerFactory.getLogger(VitalSignsFedListener.class);

    private final Storage vitalSignsStorage;
    private final Storage physiologyStorage;
    private final HlaVitalSignsManager vitalSignsManager;
    private final Set<String> transferredInstanceNames = new HashSet<>();
    private final HashMap<String,Set<HlaVitalSignsAttributes.Attribute>> attributesRequestedByInstanceName = new HashMap<>();

    public VitalSignsFedListener(StorageService storageService, HlaVitalSignsManager manager) {
        this.vitalSignsStorage = storageService.getStorage("VitalSigns");
        this.physiologyStorage = storageService.getStorage("Physiology");
        this.vitalSignsManager = manager;
        this.vitalSignsStorage.setWebItemToHlaCallback(this);
        this.vitalSignsStorage.setOwnershipToHlaCallback(this);
        manager.setHlaVitalSignsDefaultOwnershipListener(this);
    }

    @Override
    public void attributesUpdated(HlaVitalSigns hlaVital, Set<HlaVitalSignsAttributes.Attribute> attributes, HlaTimeStamp timeStamp, HlaLogicalTime logicalTime) {
        logger.info("Attributes updated for VitalSigns instance: {}", hlaVital.getHlaInstanceName());
        if (!hlaVital.isLocal() || transferredInstanceNames.contains(hlaVital.getHlaInstanceName())) {
            vitalSignsStorage.updateFromHla(hlaVital.getHlaInstanceName(), item -> populateWebItemFromHla(hlaVital, attributes, item));
            physiologyStorage.updateFromHla(hlaVital.getPatientId("ANON"),item -> populateWebItemFromHla(hlaVital, attributes, item));
        }
    }

    private void populateWebItemFromHla(HlaVitalSigns hlaVital, Set<HlaVitalSignsAttributes.Attribute> set, WebItem webItem) {
        for(HlaVitalSignsAttributes.Attribute att: set){
            if (att == null) {
                logger.warn("Received null attribute in updateWebItem. Skipping.");
                continue;
            }
            if(webItem.get("ownershipState") == null){
                webItem.put("ownershipState",OwnershipStateEnum.DISCOVERED);
            }
            switch(att){
                case HEART_RATE -> webItem.put(att.getName(), hlaVital.getHeartRate());
                case PATIENT_ID -> webItem.put(att.getName(), hlaVital.getPatientId());
                case SYSTOLIC_BLOOD_PRESSURE -> webItem.put(att.getName(), hlaVital.getSystolicBloodPressure());
                case TEMPERATURE_FAHRENHEIT -> webItem.put(att.getName(), hlaVital.getTemperatureFahrenheit());
                case RESPIRATION_RATE -> webItem.put(att.getName(), hlaVital.getRespirationRate());
                case DIASTOLIC_BLOOD_PRESSURE -> webItem.put(att.getName(), hlaVital.getDiastolicBloodPressure());
                case PERIPHERAL_OXYGEN_SATURATION -> webItem.put(att.getName(), hlaVital.getPeripheralOxygenSaturation());
                case RESPIRATION_END_TIDAL_CARBON_DIOXIDE -> webItem.put(att.getName(), hlaVital.getRespirationEndTidalCarbonDioxide());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaVitalSigns vitalSigns = this.vitalSignsManager.getVitalSignsByHlaInstanceName(webItem.getInstanceName());
        HlaVitalSignsUpdater vitalSignsUpdater = vitalSigns.getHlaVitalSignsUpdater();
        updateAndSend(webItem, vitalSignsUpdater);
    }

    public void v2PhysiologyHandlerSendToHla(WebItem physiologyWebItem){
        //webItem is physiology, need get the instance name from the vitalSings WebItem we can get the
        //correct HLA Object
        vitalSignsStorage.getAllItems()
                .stream()
                .filter((vitem)->vitem.get("patientId").equals(physiologyWebItem.get("patientId")))
                .findFirst().ifPresent((vsWebitem)->{
                    HlaVitalSigns vitalSigns = this.vitalSignsManager.getVitalSignsByHlaInstanceName(vsWebitem.getInstanceName());
                    HlaVitalSignsUpdater vitalSignsUpdater = vitalSigns.getHlaVitalSignsUpdater();
                    updateAndSend(physiologyWebItem, vitalSignsUpdater);
                });
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaVitalSigns vitalSigns = this.vitalSignsManager.createLocalHlaVitalSigns();
            HlaVitalSignsUpdater vitalSignsUpdater = vitalSigns.getHlaVitalSignsUpdater();
            updateAndSend(webItem, vitalSignsUpdater);
            return vitalSigns.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }


    private void updateAndSend(WebItem webItem, HlaVitalSignsUpdater updater) {
        webItem.getMap().forEach((key, value) -> {
            HlaVitalSignsAttributes.Attribute attribute = HlaVitalSignsAttributes.Attribute.find(key);
            if (attribute != null && value != null) {
                try {
                    switch (attribute) {
                        case HEART_RATE -> updater.setHeartRate(Integer.parseInt(value.toString()));
                        case PATIENT_ID -> updater.setPatientId(value.toString());
                        case DIASTOLIC_BLOOD_PRESSURE -> updater.setDiastolicBloodPressure(Integer.parseInt(value.toString()));
                        case SYSTOLIC_BLOOD_PRESSURE -> updater.setSystolicBloodPressure(Integer.parseInt(value.toString()));
                        case PERIPHERAL_OXYGEN_SATURATION -> updater.setPeripheralOxygenSaturation(Float.parseFloat(value.toString()));
                        case TEMPERATURE_FAHRENHEIT -> updater.setTemperatureFahrenheit(Float.parseFloat(value.toString()));
                        case RESPIRATION_RATE -> updater.setRespirationRate(Float.parseFloat(value.toString()));
                        case RESPIRATION_END_TIDAL_CARBON_DIOXIDE-> updater.setRespirationEndTidalCarbonDioxide(Float.parseFloat(value.toString()));
                        default -> logger.warn("Unhandled attribute: {}", attribute.getName());
                    }
                } catch (NumberFormatException e) {
                    logger.error("Invalid value format for attribute: {}", attribute.getName(), e);
                }
            }
        });

        try {
            updater.sendUpdate();
            logger.info("HLA update sent for instance: {}", webItem.getInstanceName());
        } catch (Exception e) {
            logger.error("Error during HLA update for instance: {}", webItem.getInstanceName(), e);
        }
    }

    //-----------------------------------------------------------------------------
    //                  Ownership Callback
    //-----------------------------------------------------------------------------

    @Override
    public void acquire(String instanceName) {
        HlaVitalSigns vitalSigns = vitalSignsManager.getVitalSignsByHlaInstanceName(instanceName);
        if(vitalSigns != null){
            try {
                logger.info("Sending acquire request for " + vitalSigns.getHlaInstanceName());
                vitalSigns.acquireOwnership(getOwnershipSet());
                vitalSignsStorage.updateFromHla(instanceName,(item)->item.put("ownershipState", OwnershipStateEnum.RELEASE_OWNERSHIP_REQUESTED));
            }catch (Exception e){
                throw new RuntimeException("acquire failed", e);
            }
        }else{
            throw new RuntimeException("VitalSigns not found for instance name: " + instanceName);
        }
    }

    @Override
    public void release(String instanceName) {
        HlaVitalSigns vitalSigns = vitalSignsManager.getVitalSignsByHlaInstanceName(instanceName);
        if(vitalSigns != null){
            try {
                logger.info("Sending release request for " + vitalSigns.getHlaInstanceName());
                Set<HlaVitalSignsAttributes.Attribute> attributes = attributesRequestedByInstanceName.get(instanceName);
                if (attributes == null) {
                    logger.info("HLA acquireOwnership request was not recieved for instance {}, releasing full set", instanceName);
                    attributes = getOwnershipSet();
                }
                vitalSigns.releaseOwnership(attributes);
                this.transferredInstanceNames.add(instanceName);
                vitalSignsStorage.updateFromHla(instanceName,(item)->item.put("ownershipState", OwnershipStateEnum.OWNERSHIP_RELEASED));
            }catch (Exception e){
                throw new RuntimeException("release failed", e);
            }
        }else{
            throw new RuntimeException("VitalSigns not found for instance name: " + instanceName);
        }
    }

    private Set<HlaVitalSignsAttributes.Attribute> getOwnershipSet(){
        return Arrays.stream(HlaVitalSignsAttributes.Attribute.values())
                .filter((att)->!att.equals(HlaVitalSignsAttributes.Attribute.HLA_PRIVILEGE_TO_DELETE_OBJECT))
                .collect(Collectors.toSet());
    }

    //-------------------------------------------------------------------------------------------
    //  HlaVitalSignsOwnershipListener
    //-------------------------------------------------------------------------------------------


    @Override
    public void releaseOwnershipRequested(HlaVitalSigns hlaVitalSigns, Set<HlaVitalSignsAttributes.Attribute> set, HlaUserSuppliedTag<Object> hlaUserSuppliedTag) {
        attributesRequestedByInstanceName.put(hlaVitalSigns.getHlaInstanceName(), set);
        vitalSignsStorage.updateFromHla(hlaVitalSigns.getHlaInstanceName(),(item)->item.put("ownershipState", OwnershipStateEnum.RELEASE_OWNERSHIP_REQUESTED));
    }

    @Override
    public void ownershipAcquired(HlaVitalSigns hlaVitalSigns, Set<HlaVitalSignsAttributes.Attribute> set, HlaUserSuppliedTag<Object> hlaUserSuppliedTag) {
        transferredInstanceNames.remove(hlaVitalSigns.getHlaInstanceName());
        vitalSignsStorage.updateFromHla(hlaVitalSigns.getHlaInstanceName(),(item)->item.put("ownershipState", OwnershipStateEnum.OWNERSHIP_ACQUIRED));
    }

    @Override
    public void ownershipOffered(HlaVitalSigns hlaVitalSigns, Set<HlaVitalSignsAttributes.Attribute> set, HlaUserSuppliedTag<Object> hlaUserSuppliedTag) {
        vitalSignsStorage.updateFromHla(hlaVitalSigns.getHlaInstanceName(),(item)->item.put("ownershipState", OwnershipStateEnum.OWNERSHIP_OFFERED));
    }

    @Override
    public void attributeOwnershipDenied(HlaVitalSigns hlaVitalSigns, Set<HlaVitalSignsAttributes.Attribute> set) {
        vitalSignsStorage.updateFromHla(hlaVitalSigns.getHlaInstanceName(),(item)->item.put("ownershipState", OwnershipStateEnum.ATTRIBUTE_OWNERSHIP_DENIED));
    }

    @Override
    public void cancelAcquireOwnershipSucceeded(HlaVitalSigns hlaVitalSigns, Set<HlaVitalSignsAttributes.Attribute> set) {
        vitalSignsStorage.updateFromHla(hlaVitalSigns.getHlaInstanceName(),(item)->item.put("ownershipState", OwnershipStateEnum.CANCEL_ACQUIRE_OWNERSHIP_SUCCEEDED));
    }
}
