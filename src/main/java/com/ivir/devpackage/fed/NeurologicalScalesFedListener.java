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
import devstudio.generatedcode.datatypes.*;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NeurologicalScalesFedListener implements HlaNeurologicalScalesListener, WebItemToHlaCallback {
    private HlaNeurologicalScalesManager neurologicalScalesManager;

    private Storage neurologicalScalesStorage;

    public NeurologicalScalesFedListener(Storage storage, HlaNeurologicalScalesManager neurologicalScalesManager){
        this.neurologicalScalesStorage = storage;
        this.neurologicalScalesManager = neurologicalScalesManager;
        this.neurologicalScalesStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaNeurologicalScales hlaNeurologicalScales, Set<HlaNeurologicalScalesAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaNeurologicalScales.isLocal()) {
            neurologicalScalesStorage.updateFromHla(hlaNeurologicalScales.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaNeurologicalScales, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaNeurologicalScales hlaNeurologicalScales, Set<HlaNeurologicalScalesAttributes.Attribute> set, WebItem webItem){
        for(HlaNeurologicalScalesAttributes.Attribute att : set){
            switch (att){
                case GLASGOW_COMA_SCALE -> webItem.put(att.getName(),
                        toSerializableObject(hlaNeurologicalScales.getGlasgowComaScale()));
                case LEVEL_OF_CONSCIOUSNESS -> webItem.put(att.getName(),hlaNeurologicalScales.getLevelOfConsciousness());
                case LEVEL_OF_RESPONSE -> webItem.put(att.getName(),hlaNeurologicalScales.getLevelOfResponse());
                case PATIENT_ID -> webItem.put(att.getName(),hlaNeurologicalScales.getPatientId());
            }
        }
    }

    private Map<String, Object> toSerializableObject(GlasgowComaScaleRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("eyes", record.getEyes());
        outMap.put("motor", record.getMotor());
        outMap.put("verbal", record.getVerbal());
        return outMap;
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaNeurologicalScales neurologicalScales = this.neurologicalScalesManager.getNeurologicalScalesByHlaInstanceName(webItem.getInstanceName());
        HlaNeurologicalScalesUpdater neurologicalScalesUpdater = neurologicalScales.getHlaNeurologicalScalesUpdater();
        updateAndSend(webItem, neurologicalScalesUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaNeurologicalScales neurologicalScales = this.neurologicalScalesManager.createLocalHlaNeurologicalScales();
            HlaNeurologicalScalesUpdater neurologicalScalesUpdater = neurologicalScales.getHlaNeurologicalScalesUpdater();
            updateAndSend(webItem, neurologicalScalesUpdater);
            return neurologicalScales.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaNeurologicalScalesUpdater neurologicalScalesUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaNeurologicalScalesAttributes.Attribute att = HlaNeurologicalScalesAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case GLASGOW_COMA_SCALE -> neurologicalScalesUpdater.setGlasgowComaScale(toGlasgowComaScaleRecord(value));
                    case LEVEL_OF_CONSCIOUSNESS -> neurologicalScalesUpdater.setLevelOfConsciousness(toLevelOfConsciousnessEnum(value));
                    case LEVEL_OF_RESPONSE -> neurologicalScalesUpdater.setLevelOfResponse(toLevelOfResponseEnum(value));
                    case PATIENT_ID -> neurologicalScalesUpdater.setPatientId((String)value);
                }
            }
        });
        try {
            neurologicalScalesUpdater.sendUpdate();
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

    private GlasgowComaScaleRecord toGlasgowComaScaleRecord(Object value){
        if(value == null){
            return null;
        }
        Map<String, Object> map = (Map<String, Object>)value;
        return GlasgowComaScaleRecord.create(
                getIntValue(map,"eyes"),
                getIntValue(map,"verbal"),
                getIntValue(map,"motor")
        );

    }

    private LevelOfConsciousnessEnum toLevelOfConsciousnessEnum(Object value){
        if(value == null){
            return null;
        }
        return LevelOfConsciousnessEnum.valueOf(value.toString());
    }

    private LevelOfResponseEnum toLevelOfResponseEnum(Object value){
        if(value == null){
            return null;
        }
        return LevelOfResponseEnum.valueOf(value.toString());
    }

    private int getIntValue(Map<String,Object> map, String key) {
        Object value = map.get(key);
        if(value == null){
            return 0;
        }
        return (Integer)value;
    }

}
