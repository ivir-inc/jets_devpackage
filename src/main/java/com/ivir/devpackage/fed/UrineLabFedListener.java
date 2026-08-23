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
import com.ivir.devpackage.fed.v1.UrineLabV1;
import devstudio.generatedcode.*;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;

import java.util.Set;

import static com.ivir.devpackage.fed.FedListenerUtility.toFloat;
import static com.ivir.devpackage.fed.FedListenerUtility.toInteger;
import static com.ivir.devpackage.fed.FedListenerUtility.toBoolean;

public class UrineLabFedListener implements HlaUrineLabListener, WebItemToHlaCallback {
    private HlaUrineLabManager urineLabManager;

    private Storage urineLabStorage;

    public UrineLabFedListener(Storage storage, HlaUrineLabManager urineLabManager){
        this.urineLabStorage = storage;
        this.urineLabManager = urineLabManager;
        this.urineLabStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaUrineLab hlaUrineLab, Set<HlaUrineLabAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaUrineLab.isLocal()) {
            urineLabStorage.updateFromHla(hlaUrineLab.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaUrineLab, set, webItem);
            });
        }

    }

    private void updateWebItem(HlaUrineLab hlaUrineLab, Set<HlaUrineLabAttributes.Attribute> set, WebItem webItem){
        for(HlaUrineLabAttributes.Attribute att : set){
            switch (att){
                case UROBILINOGEN -> webItem.put(att.getName(),hlaUrineLab.getUrobilinogen());
                case URINE_GLUCOSE -> webItem.put(att.getName(),hlaUrineLab.getUrineGlucose());
                case BILIRUBIN -> webItem.put(att.getName(),hlaUrineLab.getBilirubin());
                case URINE_KETONES -> webItem.put(att.getName(),hlaUrineLab.getUrineKetones());
                case SPECIFIC_GRAVITY -> webItem.put(att.getName(),hlaUrineLab.getSpecificGravity());
                case BLOOD_IN_URINE -> webItem.put(att.getName(),hlaUrineLab.getBloodInUrine());
                case HEMOLYSIS -> webItem.put(att.getName(),hlaUrineLab.getHemolysis());
                case URINE_PH -> webItem.put(att.getName(),hlaUrineLab.getUrinePh());
                case PROTEIN -> webItem.put(att.getName(),hlaUrineLab.getProtein());
                case NITRITE -> webItem.put(att.getName(),hlaUrineLab.getNitrite());
                case LEUKOCYTES -> webItem.put(att.getName(),hlaUrineLab.getLeukocytes());
                case ASCORBIC_ACID -> webItem.put(att.getName(),hlaUrineLab.getAscorbicAcid());
                case URINE_SODIUM -> webItem.put(att.getName(),hlaUrineLab.getUrineSodium());
                case URINE_CHLORIDE -> webItem.put(att.getName(),hlaUrineLab.getUrineChloride());
                case URINE_CREATININE -> webItem.put(att.getName(),hlaUrineLab.getUrineCreatinine());
                case URINE_BICARBONATE -> webItem.put(att.getName(),hlaUrineLab.getUrineBicarbonate());
                case AMMONIA -> webItem.put(att.getName(),hlaUrineLab.getAmmonia());
                case URINE_PHOSPHATE -> webItem.put(att.getName(),hlaUrineLab.getUrinePhosphate());
                case URINE_UREA_NITROGEN -> webItem.put(att.getName(),hlaUrineLab.getUrineUreaNitrogen());
                case PATIENT_ID -> webItem.put(att.getName(),hlaUrineLab.getPatientId());
                case TIME -> webItem.put(att.getName(),hlaUrineLab.getTime());
            }
            UrineLabV1.updateWebItemSwitch(hlaUrineLab, webItem, att);
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaUrineLab urineLab = this.urineLabManager.getUrineLabByHlaInstanceName(webItem.getInstanceName());
        HlaUrineLabUpdater urineLabUpdater = urineLab.getHlaUrineLabUpdater();
        updateAndSend(webItem, urineLabUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaUrineLab urineLab = this.urineLabManager.createLocalHlaUrineLab();
            HlaUrineLabUpdater urineLabUpdater = urineLab.getHlaUrineLabUpdater();
            updateAndSend(webItem, urineLabUpdater);
            return urineLab.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaUrineLabUpdater urineLabUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaUrineLabAttributes.Attribute att = HlaUrineLabAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case UROBILINOGEN -> urineLabUpdater.setUrobilinogen(toFloat(value));
                    case URINE_GLUCOSE -> urineLabUpdater.setUrineGlucose(toFloat(value));
                    case BILIRUBIN -> urineLabUpdater.setBilirubin(toFloat(value));
                    case URINE_KETONES -> urineLabUpdater.setUrineKetones(toFloat(value));
                    case SPECIFIC_GRAVITY -> urineLabUpdater.setSpecificGravity(toFloat(value));
                    case BLOOD_IN_URINE -> urineLabUpdater.setBloodInUrine(toInteger(value));
                    case HEMOLYSIS -> urineLabUpdater.setHemolysis(toBoolean(value));
                    case URINE_PH -> urineLabUpdater.setUrinePh(toFloat(value));
                    case PROTEIN -> urineLabUpdater.setProtein(toFloat(value));
                    case NITRITE -> urineLabUpdater.setNitrite(toFloat(value));
                    case LEUKOCYTES -> urineLabUpdater.setLeukocytes(toInteger(value));
                    case ASCORBIC_ACID -> urineLabUpdater.setAscorbicAcid(toFloat(value));
                    case URINE_SODIUM -> urineLabUpdater.setUrineSodium(toFloat(value));
                    case URINE_CHLORIDE ->  urineLabUpdater.setUrineChloride(toFloat(value));
                    case URINE_CREATININE -> urineLabUpdater.setUrineCreatinine(toFloat(value));
                    case URINE_BICARBONATE -> urineLabUpdater.setUrineBicarbonate(toFloat(value));
                    case AMMONIA -> urineLabUpdater.setAmmonia(toFloat(value));
                    case URINE_PHOSPHATE -> urineLabUpdater.setUrinePhosphate(toFloat(value));
                    case URINE_UREA_NITROGEN -> urineLabUpdater.setUrineUreaNitrogen(toFloat(value));
                    case PATIENT_ID -> urineLabUpdater.setPatientId((String)value);
                    case TIME -> urineLabUpdater.setTime((long)value);
                }
            }else{
                //v1 deprecated values
                UrineLabV1.updateAndSendKeySwitch(urineLabUpdater,key, value);
            }
        });
        try {
            urineLabUpdater.sendUpdate();
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
