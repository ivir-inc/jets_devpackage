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
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

public class DateTimeFedListener implements HlaDateTimeListener, WebItemToHlaCallback {
    private HlaDateTimeManager dateTimeManager;

    private Storage dateTimeStorage;

    public DateTimeFedListener(Storage storage, HlaDateTimeManager dateTimeManager){
        this.dateTimeStorage = storage;
        this.dateTimeManager = dateTimeManager;
        this.dateTimeStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaDateTime hlaDateTime, Set<HlaDateTimeAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaDateTime.isLocal()) {
            dateTimeStorage.updateFromHla(hlaDateTime.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaDateTime, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaDateTime hlaDateTime, Set<HlaDateTimeAttributes.Attribute> set, WebItem webItem){
        for(HlaDateTimeAttributes.Attribute att : set){
            switch (att){
                case CURRENT_DATE_TIME -> webItem.put(att.getName(),hlaDateTime.getCurrentDateTime());
                case SIMULATED_DATE_TIME -> webItem.put(att.getName(),hlaDateTime.getSimulatedDateTime());
                case SIMULATION_ELAPSED_TIME -> webItem.put(att.getName(),hlaDateTime.getSimulationElapsedTime());
                case TIME_SCALE -> webItem.put(att.getName(),hlaDateTime.getTimeScale());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaDateTime dateTime = this.dateTimeManager.getDateTimeByHlaInstanceName(webItem.getInstanceName());
        HlaDateTimeUpdater dateTimeUpdater = dateTime.getHlaDateTimeUpdater();
        updateAndSend(webItem, dateTimeUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaDateTime dateTime = this.dateTimeManager.createLocalHlaDateTime();
            HlaDateTimeUpdater dateTimeUpdater = dateTime.getHlaDateTimeUpdater();
            updateAndSend(webItem, dateTimeUpdater);
            return dateTime.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaDateTimeUpdater dateTimeUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaDateTimeAttributes.Attribute att = HlaDateTimeAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case CURRENT_DATE_TIME -> dateTimeUpdater.setCurrentDateTime((long)value);
                    case SIMULATED_DATE_TIME -> dateTimeUpdater.setSimulatedDateTime((long)value);
                    case SIMULATION_ELAPSED_TIME -> dateTimeUpdater.setSimulationElapsedTime((long)value);
                    case TIME_SCALE -> dateTimeUpdater.setTimeScale((int)value);
                }
            }
        });
        try {
            dateTimeUpdater.sendUpdate();
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
