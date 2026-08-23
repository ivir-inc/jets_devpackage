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

public class FederateDataFedListener implements HlaFederateDataListener, WebItemToHlaCallback {
    private HlaFederateDataManager federateDataManager;

    private Storage federateDataStorage;

    public FederateDataFedListener(Storage storage, HlaFederateDataManager federateDataManager){
        this.federateDataStorage = storage;
        this.federateDataManager = federateDataManager;
        this.federateDataStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaFederateData hlaFederateData, Set<HlaFederateDataAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaFederateData.isLocal()) {
            federateDataStorage.updateFromHla(hlaFederateData.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaFederateData, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaFederateData hlaFederateData, Set<HlaFederateDataAttributes.Attribute> set, WebItem webItem){
        for(HlaFederateDataAttributes.Attribute att : set){
            switch (att){
                case ID -> webItem.put(att.getName(),hlaFederateData.getId());
                case PAYLOAD -> webItem.put(att.getName(),hlaFederateData.getPayload());
                case TYPE -> webItem.put(att.getName(),hlaFederateData.getType());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaFederateData federateData = this.federateDataManager.getFederateDataByHlaInstanceName(webItem.getInstanceName());
        HlaFederateDataUpdater federateDataUpdater = federateData.getHlaFederateDataUpdater();
        updateAndSend(webItem, federateDataUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaFederateData federateData = this.federateDataManager.createLocalHlaFederateData();
            HlaFederateDataUpdater federateDataUpdater = federateData.getHlaFederateDataUpdater();
            updateAndSend(webItem, federateDataUpdater);
            return federateData.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaFederateDataUpdater federateDataUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaFederateDataAttributes.Attribute att = HlaFederateDataAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case ID -> federateDataUpdater.setId((String)value);
                    case PAYLOAD -> federateDataUpdater.setPayload((String)value);
                    case TYPE -> federateDataUpdater.setType((String)value);
                }
            }
        });
        try {
            federateDataUpdater.sendUpdate();
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
