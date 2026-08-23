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
import devstudio.generatedcode.datatypes.EventTypeEnum;
import devstudio.generatedcode.datatypes.LearnerActionEnum;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

public class EventFedListener implements HlaEventListener, WebItemToHlaCallback {
    private HlaEventManager eventManager;

    private Storage eventStorage;

    public EventFedListener(Storage storage, HlaEventManager eventManager){
        this.eventStorage = storage;
        this.eventManager = eventManager;
        this.eventStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaEvent hlaEvent, Set<HlaEventAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaEvent.isLocal()) {
            eventStorage.updateFromHla(hlaEvent.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaEvent, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaEvent hlaEvent, Set<HlaEventAttributes.Attribute> set, WebItem webItem){
        for(HlaEventAttributes.Attribute att : set){
            Object value = null;
            switch (att){
                case DESCRIPTION -> value = hlaEvent.getDescription();
                case INSTRUCTOR_ID -> value = hlaEvent.getInstructorId();
                case LEARNER_ID -> value = hlaEvent.getLearnerId();
                case NOTES -> value = hlaEvent.getNotes();
                case PATIENT_ID -> value = hlaEvent.getPatientId();
                case SIM_TIME -> value = hlaEvent.getSimTime();
                case SOURCE -> value = hlaEvent.getSource();
                case TEAM_ID -> value = hlaEvent.getTeamId();
                case TIME -> value = hlaEvent.getTime();
                case TRAINING_FACILITY_ID -> value = hlaEvent.getTrainingFacilityId();
                case TYPE -> value = hlaEvent.getType();
                case LEARNER_ACTION -> {
                    var action = hlaEvent.getLearnerAction();
                    value = (action != null) ? action.name() : null; // Handle null enums explicitly
                }
            }

            if (value != null) {
                webItem.put(att.getName(), value); // Only update if value is not null
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaEvent event = this.eventManager.getEventByHlaInstanceName(webItem.getInstanceName());
        HlaEventUpdater eventUpdater = event.getHlaEventUpdater();
        updateAndSend(webItem, eventUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaEvent event = this.eventManager.createLocalHlaEvent();
            HlaEventUpdater eventUpdater = event.getHlaEventUpdater();
            updateAndSend(webItem, eventUpdater);
            return event.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaEventUpdater eventUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaEventAttributes.Attribute att = HlaEventAttributes.Attribute.find(key);

            if (att != null && value != null) { // Skip null attributes or values
                switch (att){
                    case DESCRIPTION -> eventUpdater.setDescription((String)value);
                    case INSTRUCTOR_ID -> eventUpdater.setInstructorId((String)value);
                    case LEARNER_ID -> eventUpdater.setLearnerId((String)value);
                    case NOTES -> eventUpdater.setNotes((String)value);
                    case PATIENT_ID -> eventUpdater.setPatientId((String)value);
                    case SIM_TIME -> eventUpdater.setSimTime((long)value);
                    case SOURCE -> eventUpdater.setSource((String)value);
                    case TEAM_ID -> eventUpdater.setTeamId((String)value);
                    case TIME -> eventUpdater.setTime((long)value);
                    case TYPE -> eventUpdater.setType(toEventTypeEnum(value));
                    case TRAINING_FACILITY_ID -> eventUpdater.setTrainingFacilityId((String)value);
                    case LEARNER_ACTION -> eventUpdater.setLearnerAction(toLearnerActionEnum(value));
                }
            }
        });

        try {
            eventUpdater.sendUpdate();
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private EventTypeEnum toEventTypeEnum(Object value){
        if (value == null) return null; // Null indicates no update has been made
        try {
            return EventTypeEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the value cannot be mapped
        }
        }

    private LearnerActionEnum toLearnerActionEnum(Object value) {
        if (value == null) return null; // Null indicates no update has been made
        try {
            return LearnerActionEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Return null if the value cannot be mapped
        }
    }

}
