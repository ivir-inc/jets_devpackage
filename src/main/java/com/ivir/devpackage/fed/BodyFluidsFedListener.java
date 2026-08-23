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

import static com.ivir.devpackage.fed.FedListenerUtility.*;

public class BodyFluidsFedListener implements HlaBodyFluidsListener, WebItemToHlaCallback {
    private HlaBodyFluidsManager bodyFluidsManager;

    private Storage bodyFluidsStorage;

    public BodyFluidsFedListener(Storage storage, HlaBodyFluidsManager bodyFluidsManager){
        this.bodyFluidsStorage = storage;
        this.bodyFluidsManager = bodyFluidsManager;
        this.bodyFluidsStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaBodyFluids hlaBodyFluids, Set<HlaBodyFluidsAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaBodyFluids.isLocal()) {
            bodyFluidsStorage.updateFromHla(hlaBodyFluids.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaBodyFluids, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaBodyFluids hlaBodyFluids, Set<HlaBodyFluidsAttributes.Attribute> set, WebItem webItem){
        for(HlaBodyFluidsAttributes.Attribute att : set){
            switch (att){
                case BLOOD_LOSS_RATE -> webItem.put(att.getName(),hlaBodyFluids.getBloodLossRate());
                case BLOOD_VOLUME -> webItem.put(att.getName(),hlaBodyFluids.getBloodVolume());
                case SWEAT_RATE -> webItem.put(att.getName(),hlaBodyFluids.getSweatRate());
                case URINE_OUTPUT_RATE -> webItem.put(att.getName(),hlaBodyFluids.getUrineOutputRate());
                case PATIENT_ID -> webItem.put(att.getName(),hlaBodyFluids.getPatientId());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaBodyFluids bodyFluids = this.bodyFluidsManager.getBodyFluidsByHlaInstanceName(webItem.getInstanceName());
        HlaBodyFluidsUpdater bodyFluidsUpdater = bodyFluids.getHlaBodyFluidsUpdater();
        updateAndSend(webItem, bodyFluidsUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaBodyFluids bodyFluids = this.bodyFluidsManager.createLocalHlaBodyFluids();
            HlaBodyFluidsUpdater bodyFluidsUpdater = bodyFluids.getHlaBodyFluidsUpdater();
            updateAndSend(webItem, bodyFluidsUpdater);
            return bodyFluids.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaBodyFluidsUpdater bodyFluidsUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaBodyFluidsAttributes.Attribute att = HlaBodyFluidsAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case BLOOD_LOSS_RATE -> bodyFluidsUpdater.setBloodLossRate(toFloat(value));
                    case BLOOD_VOLUME -> bodyFluidsUpdater.setBloodVolume(toFloat(value));
                    case SWEAT_RATE -> bodyFluidsUpdater.setSweatRate(toFloat(value));
                    case URINE_OUTPUT_RATE -> bodyFluidsUpdater.setUrineOutputRate(toFloat(value));
                    case PATIENT_ID -> bodyFluidsUpdater.setPatientId((String)value);
                }
            }
        });
        try {
            bodyFluidsUpdater.sendUpdate();
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
