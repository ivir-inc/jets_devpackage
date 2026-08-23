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

public class DataLogFedListener implements HlaDataLogListener, WebItemToHlaCallback {
    private HlaDataLogManager dataLogManager;

    private Storage dataLogStorage;

    public DataLogFedListener(Storage storage, HlaDataLogManager dataLogManager){
        this.dataLogStorage = storage;
        this.dataLogManager = dataLogManager;
        this.dataLogStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaDataLog hlaDataLog, Set<HlaDataLogAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaDataLog.isLocal()) {
            dataLogStorage.updateFromHla(hlaDataLog.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaDataLog, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaDataLog hlaDataLog, Set<HlaDataLogAttributes.Attribute> set, WebItem webItem){
        for(HlaDataLogAttributes.Attribute att : set){
            switch (att){
                case TIME -> webItem.put(att.getName(),hlaDataLog.getTime());
                case SOURCE -> webItem.put(att.getName(),hlaDataLog.getSource());
                case DATA -> webItem.put(att.getName(),hlaDataLog.getData());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaDataLog dataLog = this.dataLogManager.getDataLogByHlaInstanceName(webItem.getInstanceName());
        HlaDataLogUpdater dataLogUpdater = dataLog.getHlaDataLogUpdater();
        updateAndSend(webItem, dataLogUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaDataLog dataLog = this.dataLogManager.createLocalHlaDataLog();
            HlaDataLogUpdater dataLogUpdater = dataLog.getHlaDataLogUpdater();
            updateAndSend(webItem, dataLogUpdater);
            return dataLog.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaDataLogUpdater dataLogUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaDataLogAttributes.Attribute att = HlaDataLogAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case TIME -> dataLogUpdater.setTime((long)value);
                    case SOURCE -> dataLogUpdater.setSource((String)value);
                    case DATA -> dataLogUpdater.setData((String)value);
                }
            }
        });
        try {
            dataLogUpdater.sendUpdate();
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
