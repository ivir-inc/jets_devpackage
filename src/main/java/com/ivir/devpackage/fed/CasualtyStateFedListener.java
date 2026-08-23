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
import devstudio.generatedcode.datatypes.EvacuationPriorityEnum;
import devstudio.generatedcode.datatypes.PhysicalLocationRecord;
import devstudio.generatedcode.datatypes.RoleOfCareEnum;
import devstudio.generatedcode.datatypes.TriageEnum;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.ivir.devpackage.fed.FedListenerUtility.toDouble;
import static com.ivir.devpackage.fed.FedListenerUtility.toWebItemValueWithNull;

public class CasualtyStateFedListener implements HlaCasualtyStateListener, WebItemToHlaCallback {
    private HlaCasualtyStateManager casualtyStateManager;

    private Storage casualtyStateStorage;

    public CasualtyStateFedListener(Storage storage, HlaCasualtyStateManager casualtyStateManager){
        this.casualtyStateStorage = storage;
        this.casualtyStateManager = casualtyStateManager;
        this.casualtyStateStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaCasualtyState hlaCasualtyState, Set<HlaCasualtyStateAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaCasualtyState.isLocal()) {
            casualtyStateStorage.updateFromHla(hlaCasualtyState.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaCasualtyState, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaCasualtyState hlaCasualtyState, Set<HlaCasualtyStateAttributes.Attribute> set, WebItem webItem){
        for(HlaCasualtyStateAttributes.Attribute att : set){
            Object value = null;
            switch (att){
                case FACILITY_ID -> value = hlaCasualtyState.getFacilityId();
                case EVACUATION_PRIORITY-> value = FedListenerUtility.toWebItemValueWithNotApplicable(hlaCasualtyState.getEvacuationPriority());
                case PATIENT_ID -> value = hlaCasualtyState.getPatientId();
                case TRIAGE_CLASSIFICATION -> value = FedListenerUtility.toWebItemValueWithNull(hlaCasualtyState.getTriageClassification());
            }

            if (value != null) {
                webItem.put(att.getName(), value); // Only update if value is not null
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaCasualtyState casualtyState = this.casualtyStateManager.getCasualtyStateByHlaInstanceName(webItem.getInstanceName());
        HlaCasualtyStateUpdater casualtyStateUpdater = casualtyState.getHlaCasualtyStateUpdater();
        updateAndSend(webItem, casualtyStateUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaCasualtyState casualtyState = this.casualtyStateManager.createLocalHlaCasualtyState();
            HlaCasualtyStateUpdater casualtyStateUpdater = casualtyState.getHlaCasualtyStateUpdater();
            updateAndSend(webItem, casualtyStateUpdater);
            return casualtyState.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaCasualtyStateUpdater casualtyStateUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaCasualtyStateAttributes.Attribute att = HlaCasualtyStateAttributes.Attribute.find(key);

            if (att != null && value != null) { // Skip null attributes or values
                switch (att){
                    case FACILITY_ID -> casualtyStateUpdater.setFacilityId((String)value);
                    case PATIENT_ID-> casualtyStateUpdater.setPatientId((String)value);
                    case EVACUATION_PRIORITY -> casualtyStateUpdater.setEvacuationPriority(toEvacuationPriority(value));
                    case TRIAGE_CLASSIFICATION -> casualtyStateUpdater.setTriageClassification(toTriage(value));
                }
            }
        });

        try {
            casualtyStateUpdater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private EvacuationPriorityEnum toEvacuationPriority(Object value){
        if(value == null) return null;
        try{
            return EvacuationPriorityEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the value cannot be mapped
        }
    }

    private TriageEnum toTriage(Object value){
        if(value == null) return null;
        try{
            return TriageEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the value cannot be mapped
        }
    }
}
