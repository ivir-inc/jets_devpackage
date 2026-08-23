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

import static com.ivir.devpackage.fed.FedListenerUtility.toFloat;

public class BloodLabFedListener implements HlaBloodLabListener, WebItemToHlaCallback {
    private HlaBloodLabManager bloodLabManager;

    private Storage bloodGasStorage;

    public BloodLabFedListener(Storage storage, HlaBloodLabManager bloodLabManager){
        this.bloodGasStorage = storage;
        this.bloodLabManager = bloodLabManager;
        this.bloodGasStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaBloodLab hlaBloodLab, Set<HlaBloodLabAttributes.Attribute> set, HlaTimeStamp hlaTimeStamp, HlaLogicalTime hlaLogicalTime) {
        if(!hlaBloodLab.isLocal()) {
            bloodGasStorage.updateFromHla(hlaBloodLab.getHlaInstanceName(), (webItem) -> {
                updateWebItem(hlaBloodLab, set, webItem);
            });
        }
    }

    private void updateWebItem(HlaBloodLab hlaBloodLab, Set<HlaBloodLabAttributes.Attribute> set, WebItem webItem){
        for(HlaBloodLabAttributes.Attribute att : set){
            switch (att){
                case BASE_EXCESS -> webItem.put(att.getName(),hlaBloodLab.getBaseExcess());
                case BLOOD_BICARBONATE -> {
                    webItem.put(att.getName(),hlaBloodLab.getBloodBicarbonate());
                    //backwards support for v1
                    webItem.put("bicarbonate",hlaBloodLab.getBloodBicarbonate());
                }
                case BLOOD_CHLORIDE -> webItem.put(att.getName(),hlaBloodLab.getBloodChloride());
                case BLOOD_CREATININE -> webItem.put(att.getName(),hlaBloodLab.getBloodCreatinine());
                case BLOOD_GLUCOSE -> webItem.put(att.getName(),hlaBloodLab.getBloodGlucose());
                case BLOOD_KETONES -> webItem.put(att.getName(),hlaBloodLab.getBloodKetones());
                case BLOOD_PH -> webItem.put(att.getName(),hlaBloodLab.getBloodPh());
                case BLOOD_SODIUM -> {
                    webItem.put(att.getName(), hlaBloodLab.getBloodSodium());
                    //backwards support for v1
                    webItem.put("sodium", hlaBloodLab.getBloodSodium());
                }
                case BLOOD_UREA_NITROGEN -> webItem.put(att.getName(),hlaBloodLab.getBloodUreaNitrogen());
                case BLOOD_PHOSPHATE-> webItem.put(att.getName(),hlaBloodLab.getBloodPhosphate());
                case FATTY_ACIDS -> webItem.put(att.getName(),hlaBloodLab.getFattyAcids());
                case HEMATOCRIT -> webItem.put(att.getName(),hlaBloodLab.getHematocrit());
                case HEMOGLOBIN -> webItem.put(att.getName(),hlaBloodLab.getHemoglobin());
                case IONIZED_CALCIUM -> webItem.put(att.getName(),hlaBloodLab.getIonizedCalcium());
                case LACTATE -> webItem.put(att.getName(),hlaBloodLab.getLactate());
                case POTASSIUM -> webItem.put(att.getName(),hlaBloodLab.getPotassium());
                case PATIENT_ID -> webItem.put(att.getName(),hlaBloodLab.getPatientId());
                case TIME -> webItem.put(att.getName(),hlaBloodLab.getTime());
                case TRIGLYCERIDES -> webItem.put(att.getName(),hlaBloodLab.getTriglycerides());
            }
        }
    }

    @Override
    public void sendToHla(WebItem webItem) {
        HlaBloodLab bloodLab = this.bloodLabManager.getBloodLabByHlaInstanceName(webItem.getInstanceName());
        HlaBloodLabUpdater bloodLabUpdater = bloodLab.getHlaBloodLabUpdater();
        updateAndSend(webItem, bloodLabUpdater);
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaBloodLab bloodLab = this.bloodLabManager.createLocalHlaBloodLab();
            HlaBloodLabUpdater bloodLabUpdater = bloodLab.getHlaBloodLabUpdater();
            updateAndSend(webItem, bloodLabUpdater);
            return bloodLab.getHlaInstanceName();
        } catch (HlaNotConnectedException e) {
            throw new RuntimeException(e);
        } catch (HlaInternalException e) {
            throw new RuntimeException(e);
        } catch (HlaRtiException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaBloodLabUpdater bloodLabUpdater){
        webItem.getMap().forEach((key,value)->{
            HlaBloodLabAttributes.Attribute att = HlaBloodLabAttributes.Attribute.find(key);
            if((att != null) && (value != null)){
                switch (att){
                    case BASE_EXCESS -> bloodLabUpdater.setBaseExcess(toFloat(value));
                    case BLOOD_BICARBONATE -> bloodLabUpdater.setBloodBicarbonate(toFloat(value));
                    case BLOOD_CHLORIDE -> bloodLabUpdater.setBloodChloride(toFloat(value));
                    case BLOOD_CREATININE -> bloodLabUpdater.setBloodCreatinine(toFloat(value));
                    case BLOOD_GLUCOSE -> bloodLabUpdater.setBloodGlucose(toFloat(value));
                    case BLOOD_KETONES -> bloodLabUpdater.setBloodKetones(toFloat(value));
                    case BLOOD_PH -> bloodLabUpdater.setBloodPh(toFloat(value));
                    case BLOOD_PHOSPHATE -> bloodLabUpdater.setBloodPhosphate(toFloat(value));
                    case BLOOD_SODIUM -> bloodLabUpdater.setBloodSodium(toFloat(value));
                    case BLOOD_UREA_NITROGEN -> bloodLabUpdater.setBloodUreaNitrogen(toFloat(value));
                    case FATTY_ACIDS -> bloodLabUpdater.setFattyAcids(toFloat(value));
                    case HEMATOCRIT -> bloodLabUpdater.setHematocrit(toFloat(value));
                    case HEMOGLOBIN -> bloodLabUpdater.setHemoglobin(toFloat(value));
                    case IONIZED_CALCIUM -> bloodLabUpdater.setIonizedCalcium(toFloat(value));
                    case LACTATE -> bloodLabUpdater.setLactate(toFloat(value));
                    case PATIENT_ID -> bloodLabUpdater.setPatientId((String)value);
                    case POTASSIUM -> bloodLabUpdater.setPotassium(toFloat(value));
                    case TIME -> bloodLabUpdater.setTime((long)value);
                    case TRIGLYCERIDES -> bloodLabUpdater.setTriglycerides(toFloat(value));
                }
            }else{
                //v1 values
                switch(key){
                    case "sodium" -> bloodLabUpdater.setBloodSodium(toFloat(value));
                    case "bicarbonate" -> bloodLabUpdater.setBloodBicarbonate(toFloat(value));
                }
            }
        });
        try {
            bloodLabUpdater.sendUpdate();
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
