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
import com.ivir.devpackage.model.StorageService;
import devstudio.generatedcode.*;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class RespiratoryPhysiologyFedListener implements HlaRespiratoryPhysiologyListener, WebItemToHlaCallback{
    private static final Logger logger = LoggerFactory.getLogger(RespiratoryPhysiologyFedListener.class);

    private final Storage respiratoryPhysiologyStorage;
    private final Storage physiologyStorage;
    private final HlaRespiratoryPhysiologyManager respiratoryPhysiologyManager;

    public RespiratoryPhysiologyFedListener(StorageService storageService, HlaRespiratoryPhysiologyManager manager) {
        this.respiratoryPhysiologyStorage = storageService.getStorage("RespiratoryPhysiology");
        this.physiologyStorage = storageService.getStorage("Physiology");
        this.respiratoryPhysiologyManager = manager;
        this.respiratoryPhysiologyStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaRespiratoryPhysiology hlaRespiratory, Set<HlaRespiratoryPhysiologyAttributes.Attribute> attributes, HlaTimeStamp timeStamp, HlaLogicalTime logicalTime) {
        logger.info("Attributes updated for RespiratoryPhysiology instance: {}", hlaRespiratory.getHlaInstanceName());
        if (!hlaRespiratory.isLocal()) {
            respiratoryPhysiologyStorage.updateFromHla(hlaRespiratory.getHlaInstanceName(), item -> populateWebItemFromHla(hlaRespiratory, attributes, item));
            physiologyStorage.updateFromHla(hlaRespiratory.getPatientID("ANON"),item -> populateWebItemFromHla(hlaRespiratory, attributes, item));
        }
    }

    private void populateWebItemFromHla(HlaRespiratoryPhysiology hlaRespiratory, Set<HlaRespiratoryPhysiologyAttributes.Attribute> set, WebItem webItem) {
        for(HlaRespiratoryPhysiologyAttributes.Attribute att: set){
            if (att == null) {
                logger.warn("Received null attribute in updateWebItem. Skipping.");
                continue;
            }
            switch(att){
                case LUNG_TIDAL_VOLUME -> webItem.put(att.getName(), hlaRespiratory.getLungTidalVolume());
                case PATIENT_ID -> webItem.put(att.getName(), hlaRespiratory.getPatientID());
                case LUNG_DEAD_SPACE -> webItem.put(att.getName(), hlaRespiratory.getLungDeadSpace());
                case LUNG_EXPIRATORY_RESERVE -> webItem.put(att.getName(), hlaRespiratory.getLungExpiratoryReserve());
                case LUNG_INSPIRATORY_RESERVE -> webItem.put(att.getName(), hlaRespiratory.getLungInspiratoryReserve());
                case LUNG_RESIDUAL_VOLUME -> webItem.put(att.getName(), hlaRespiratory.getLungResidualVolume());
                case LUNG_TOTAL_CAPACITY -> webItem.put(att.getName(), hlaRespiratory.getLungTotalCapacity());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaRespiratoryPhysiology respiratoryPhysiology = this.respiratoryPhysiologyManager.getRespiratoryPhysiologyByHlaInstanceName(webItem.getInstanceName());
        HlaRespiratoryPhysiologyUpdater respiratoryPhysiologyUpdater = respiratoryPhysiology.getHlaRespiratoryPhysiologyUpdater();
        updateAndSend(webItem, respiratoryPhysiologyUpdater);
    }

    public void v2PhysiologyHandlerSendToHla(WebItem physiologyWebItem){
        //webItem is physiology, need get the instance name from the respiratory WebItem we can get the
        //correct HLA Object
        respiratoryPhysiologyStorage.getAllItems()
                .stream()
                .filter((rpitem)->rpitem.get("patientID").equals(physiologyWebItem.get("patientId")))
                .findFirst().ifPresent((vsWebitem)->{
                    HlaRespiratoryPhysiology respiratoryPhysiology = this.respiratoryPhysiologyManager.getRespiratoryPhysiologyByHlaInstanceName(vsWebitem.getInstanceName());
                    HlaRespiratoryPhysiologyUpdater respiratoryPhysiologyUpdater = respiratoryPhysiology.getHlaRespiratoryPhysiologyUpdater();
                    updateAndSend(physiologyWebItem, respiratoryPhysiologyUpdater);
                });
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaRespiratoryPhysiology respiratoryPhysiology = this.respiratoryPhysiologyManager.createLocalHlaRespiratoryPhysiology();
            HlaRespiratoryPhysiologyUpdater respiratoryPhysiologyUpdater = respiratoryPhysiology.getHlaRespiratoryPhysiologyUpdater();
            updateAndSend(webItem, respiratoryPhysiologyUpdater);
            return respiratoryPhysiology.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }


    private void updateAndSend(WebItem webItem, HlaRespiratoryPhysiologyUpdater updater) {
        webItem.getMap().forEach((key, value) -> {
            HlaRespiratoryPhysiologyAttributes.Attribute attribute = HlaRespiratoryPhysiologyAttributes.Attribute.find(key);
            if (attribute != null && value != null) {
                try {
                    switch (attribute) {
                        case PATIENT_ID -> updater.setPatientID(value.toString());
                        case LUNG_RESIDUAL_VOLUME -> updater.setLungResidualVolume(Integer.parseInt(value.toString()));
                        case LUNG_TOTAL_CAPACITY -> updater.setLungTotalCapacity(Integer.parseInt(value.toString()));
                        case LUNG_DEAD_SPACE -> updater.setLungDeadSpace(Integer.parseInt(value.toString()));
                        case LUNG_EXPIRATORY_RESERVE -> updater.setLungExpiratoryReserve(Integer.parseInt(value.toString()));
                        case LUNG_INSPIRATORY_RESERVE -> updater.setLungInspiratoryReserve(Integer.parseInt(value.toString()));
                        case LUNG_TIDAL_VOLUME -> updater.setLungTidalVolume(Integer.parseInt(value.toString()));
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
}

