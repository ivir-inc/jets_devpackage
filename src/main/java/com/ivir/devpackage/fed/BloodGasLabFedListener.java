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

import static com.ivir.devpackage.fed.FedListenerUtility.*;

import com.ivir.devpackage.model.Storage;
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToHlaCallback;
import devstudio.generatedcode.*;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

public class BloodGasLabFedListener implements HlaBloodGasLabListener, WebItemToHlaCallback {
    private HlaBloodGasLabManager bloodGasLabManager;

    private Storage bloodGasStorage;

    public BloodGasLabFedListener(Storage storage, HlaBloodGasLabManager bloodGasLabManager){
        this.bloodGasStorage = storage;
        this.bloodGasLabManager = bloodGasLabManager;
        this.bloodGasStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaBloodGasLab hlaBloodGasLab, Set<HlaBloodGasLabAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaBloodGasLab.isLocal()) {
            bloodGasStorage.updateFromHla(hlaBloodGasLab.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaBloodGasLab, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaBloodGasLab hlaBloodGasLab, Set<HlaBloodGasLabAttributes.Attribute> set, WebItem webItem){
        for(HlaBloodGasLabAttributes.Attribute att : set){
            switch (att){
                case PARTIAL_PRESSURE_CARBON_DIOXIDE -> webItem.put(att.getName(),hlaBloodGasLab.getPartialPressureCarbonDioxide());
                case PARTIAL_PRESSURE_OXYGEN -> webItem.put(att.getName(),hlaBloodGasLab.getPartialPressureOxygen());
                case PATIENT_ID -> webItem.put(att.getName(),hlaBloodGasLab.getPatientId());
                case SULFUR_DIOXIDE -> webItem.put(att.getName(),hlaBloodGasLab.getSulfurDioxide());
                case TIME -> webItem.put(att.getName(),hlaBloodGasLab.getTime());
                case TOTAL_CARBON_DIOXIDE -> webItem.put(att.getName(),hlaBloodGasLab.getTotalCarbonDioxide());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaBloodGasLab bloodGasLab = this.bloodGasLabManager.getBloodGasLabByHlaInstanceName(webItem.getInstanceName());
        HlaBloodGasLabUpdater bloodGasLabUpdater = bloodGasLab.getHlaBloodGasLabUpdater();
        updateAndSend(webItem, bloodGasLabUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaBloodGasLab bloodGasLab = this.bloodGasLabManager.createLocalHlaBloodGasLab();
            HlaBloodGasLabUpdater bloodGasLabUpdater = bloodGasLab.getHlaBloodGasLabUpdater();
            updateAndSend(webItem, bloodGasLabUpdater);
            return bloodGasLab.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaBloodGasLabUpdater bloodGasLabUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaBloodGasLabAttributes.Attribute att = HlaBloodGasLabAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case PARTIAL_PRESSURE_CARBON_DIOXIDE -> bloodGasLabUpdater.setPartialPressureCarbonDioxide(toFloat(value));
                    case PARTIAL_PRESSURE_OXYGEN -> bloodGasLabUpdater.setPartialPressureOxygen(toFloat(value));
                    case PATIENT_ID -> bloodGasLabUpdater.setPatientId((String)value);
                    case SULFUR_DIOXIDE -> bloodGasLabUpdater.setSulfurDioxide(toFloat(value));
                    case TIME -> bloodGasLabUpdater.setTime((long)value);
                    case TOTAL_CARBON_DIOXIDE -> bloodGasLabUpdater.setTotalCarbonDioxide(toFloat(value));
                }
            }
        });
        try {
            bloodGasLabUpdater.sendUpdate();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaAttributeNotOwnedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

}
