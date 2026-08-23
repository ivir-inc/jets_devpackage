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
import devstudio.generatedcode.datatypes.FederationStateEnum;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

public class FederationStateFedListener implements HlaFederationStateListener, WebItemToHlaCallback {
    private HlaFederationStateManager federationStateManager;

    private Storage federationStateStorage;

    public FederationStateFedListener(Storage storage, HlaFederationStateManager federationStateManager){
        this.federationStateStorage = storage;
        this.federationStateManager = federationStateManager;
        this.federationStateStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaFederationState hlaFederationState, Set<HlaFederationStateAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaFederationState.isLocal()) {
            federationStateStorage.updateFromHla(hlaFederationState.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaFederationState, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaFederationState hlaFederationState, Set<HlaFederationStateAttributes.Attribute> set, WebItem webItem){
        for(HlaFederationStateAttributes.Attribute att : set){
            switch (att){
                case STATE -> webItem.put(att.getName(),hlaFederationState.getState());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaFederationState federationState = this.federationStateManager.getFederationStateByHlaInstanceName(webItem.getInstanceName());
        HlaFederationStateUpdater federationStateUpdater = federationState.getHlaFederationStateUpdater();
        updateAndSend(webItem, federationStateUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaFederationState federationState = this.federationStateManager.createLocalHlaFederationState();
            HlaFederationStateUpdater federationStateUpdater = federationState.getHlaFederationStateUpdater();
            updateAndSend(webItem, federationStateUpdater);
            return federationState.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaFederationStateUpdater federationStateUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaFederationStateAttributes.Attribute att = HlaFederationStateAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case STATE -> federationStateUpdater.setState(toFederationStateEnum(value));
                }
            }
        });
        try {
            federationStateUpdater.sendUpdate();
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

    private FederationStateEnum toFederationStateEnum(Object value){
        if(value == null){
            return null;
        }
        return FederationStateEnum.valueOf(value.toString());
    }

}
