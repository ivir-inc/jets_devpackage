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

public class CreatePatientFedListener implements HlaCreatePatientListener, WebItemToHlaCallback {
    private HlaCreatePatientManager createPatientManager;

    private Storage createPatientStorage;

    public CreatePatientFedListener(Storage storage, HlaCreatePatientManager createPatientManager){
        this.createPatientStorage = storage;
        this.createPatientManager = createPatientManager;
        this.createPatientStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaCreatePatient hlaCreatePatient, Set<HlaCreatePatientAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaCreatePatient.isLocal()) {
            createPatientStorage.updateFromHla(hlaCreatePatient.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaCreatePatient, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaCreatePatient hlaCreatePatient, Set<HlaCreatePatientAttributes.Attribute> set, WebItem webItem){
        for(HlaCreatePatientAttributes.Attribute att : set){
            switch (att){
                case PATIENT_ID -> webItem.put(att.getName(),hlaCreatePatient.getPatientId());
                case TARGET -> webItem.put(att.getName(),hlaCreatePatient.getTarget());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaCreatePatient createPatient = this.createPatientManager.getCreatePatientByHlaInstanceName(webItem.getInstanceName());
        HlaCreatePatientUpdater createPatientUpdater = createPatient.getHlaCreatePatientUpdater();
        updateAndSend(webItem, createPatientUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaCreatePatient createPatient = this.createPatientManager.createLocalHlaCreatePatient();
            HlaCreatePatientUpdater createPatientUpdater = createPatient.getHlaCreatePatientUpdater();
            updateAndSend(webItem, createPatientUpdater);
            return createPatient.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaCreatePatientUpdater createPatientUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaCreatePatientAttributes.Attribute att = HlaCreatePatientAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case PATIENT_ID -> createPatientUpdater.setPatientId((String)value);
                    case TARGET -> createPatientUpdater.setTarget((String)value);
                }
            }
        });
        try {
            createPatientUpdater.sendUpdate();
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
