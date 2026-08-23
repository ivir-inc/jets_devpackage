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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TacticalCombatCasualtyCareCardFedListener implements HlaTacticalCombatCasualtyCareCardListener, WebItemToHlaCallback {
    private static final Logger logger = LoggerFactory.getLogger(TacticalCombatCasualtyCareCardFedListener.class);

    private final HlaTacticalCombatCasualtyCareCardManager tacticalCombatCasualtyCareCardManager;
    private final Storage tacticalCombatCasualtyCareCardStorage;

    public TacticalCombatCasualtyCareCardFedListener(Storage storage, HlaTacticalCombatCasualtyCareCardManager tacticalCombatCasualtyCareCardManager) {
        this.tacticalCombatCasualtyCareCardStorage = storage;
        this.tacticalCombatCasualtyCareCardManager = tacticalCombatCasualtyCareCardManager;
        this.tacticalCombatCasualtyCareCardStorage.setWebItemToHlaCallback(this);
    }

    @Override
    public void attributesUpdated(HlaTacticalCombatCasualtyCareCard hlaCard, Set<HlaTacticalCombatCasualtyCareCardAttributes.Attribute> attributes, HlaTimeStamp timeStamp, HlaLogicalTime logicalTime) {
        if (!hlaCard.isLocal()) {
            logger.info("Attributes updated for instance: {}", hlaCard.getHlaInstanceName());
            tacticalCombatCasualtyCareCardStorage.updateFromHla(hlaCard.getHlaInstanceName(), (webItem) -> updateWebItem(hlaCard, attributes, webItem));
        }
    }

    private void updateWebItem(HlaTacticalCombatCasualtyCareCard hlaCard, Set<HlaTacticalCombatCasualtyCareCardAttributes.Attribute> attributes, WebItem webItem) {
        for (HlaTacticalCombatCasualtyCareCardAttributes.Attribute attribute : attributes) {
            try {
                switch (attribute) {
                    case PATIENT_ID -> webItem.put(attribute.getName(), hlaCard.getPatientId());
                    case BATTLE_ROSTER_NUMBER -> webItem.put(attribute.getName(), hlaCard.getBattleRosterNumber());
                    case EVACUATION_LEVEL_REQUEST -> webItem.put(attribute.getName(), hlaCard.getEvacuationLevelRequest());
                    case LAST_NAME -> webItem.put(attribute.getName(), hlaCard.getLastName());
                    case FIRST_NAME -> webItem.put(attribute.getName(), hlaCard.getFirstName());
                    case SOCIAL_SECURITY_ACCOUNT_NUMBER -> webItem.put(attribute.getName(), hlaCard.getSocialSecurityAccountNumber());
                    case GENDER -> webItem.put(attribute.getName(), hlaCard.getGender());
                    case DATE -> webItem.put(attribute.getName(), hlaCard.getDate());
                    case TIME -> webItem.put(attribute.getName(), hlaCard.getTime());
                    case SERVICE -> webItem.put(attribute.getName(), hlaCard.getService());
                    case UNIT -> webItem.put(attribute.getName(), hlaCard.getUnit());
                    case ALLERGIES -> webItem.put(attribute.getName(), hlaCard.getAllergies());
                    case MECHANISM_OF_INJURY -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getMechanismOfInjury()));
                    case INJURY_ANNOTATION -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getInjuryAnnotation()));
                    case SIGNS_SYMPTOMS -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getSignsSymptoms()));
                    case TREATMENT_CIRCULATORY_TOURNIQUET -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentCirculatoryTourniquet()));
                    case TREATMENT_CIRCULATORY_DRESSING -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentCirculatoryDressing()));
                    case TREATMENT_AIRWAY -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentAirway()));
                    case TREATMENT_BREATHING -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentBreathing()));
                    case TREATMENT_FLUIDS -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentFluids()));
                    case TREATMENT_BLOOD_PRODUCTS -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentBloodProducts()));
                    case TREATMENT_MEDICATIONS_ANALGESIC -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentMedicationsAnalgesic()));
                    case TREATMENT_MEDICATIONS_ANTIBIOTIC -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentMedicationsAntibiotic()));
                    case TREATMENT_MEDICATIONS_OTHER -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentMedicationsOther()));
                    case TREATMENT_OTHER -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getTreatmentOther()));
                    case TREATMENT_NOTES -> webItem.put(attribute.getName(), hlaCard.getTreatmentNotes());
                    case RESPONDER -> webItem.put(attribute.getName(),
                            toSerializableObject(hlaCard.getResponder()));
                    default -> logger.warn("Unhandled attribute: {}", attribute.getName());
                }
            } catch (Exception e) {
                logger.error("Error updating attribute: {} for instance: {}", attribute.getName(), hlaCard.getHlaInstanceName(), e);
            }
        }
    }

    private Map<String, Object> toSerializableObject(MechanismOfInjuryCommsRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("artillery", record.getArtillery());
        outMap.put("blunt", record.getBlunt());
        outMap.put("burn", record.getBurn());
        outMap.put("grenade", record.getGrenade());
        outMap.put("gunShotWound", record.getGunShotWound());
        outMap.put("improvisedExplosiveDevice", record.getImprovisedExplosiveDevice());
        outMap.put("landMine", record.getLandMine());
        outMap.put("motorVehicleCollision", record.getMotorVehicleCollision());
        outMap.put("other", record.getOther());
        outMap.put("otherCause", record.getOtherCause());
        outMap.put("rocketPropelledGrenade", record.getRocketPropelledGrenade());
        return outMap;
    }

    private Map<String, Object> toSerializableObject(InjuryAnnotationRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("annotationList", toSerializableObject(record.getAnnotationList()));
        outMap.put("leftArmTourniquet", toSerializableObject(record.getLeftArmTourniquet()));
        outMap.put("leftLegTourniquet", toSerializableObject(record.getLeftLegTourniquet()));
        outMap.put("rightArmTourniquet", toSerializableObject(record.getRightArmTourniquet()));
        outMap.put("rightLegTourniquet", toSerializableObject(record.getRightLegTourniquet()));
        return outMap;
    }

    private List<HashMap<String, Object>> toSerializableObject(InjuryLocationRecord[] records){
        return Arrays.stream(records).map((record)->{
            HashMap<String, Object> outMap = new HashMap<>();
            outMap.put("injuryLocation", Arrays.stream(record.getInjuryLocation()).toList());
            outMap.put("injuryType", record.getInjuryType());
            return  outMap;
        }).toList();
    }

    private Map<String, Object> toSerializableObject(TourniquetRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("time", record.getTime());
        outMap.put("type", record.getType());
        return outMap;
    }

    private List<HashMap<String, Object>> toSerializableObject(SignsSymptomsRecord[] records){
        return Arrays.stream(records).map((record)->{
            HashMap<String, Object> outMap = new HashMap<>();
            outMap.put("time", record.getTime());
            outMap.put("alertnessLevel", record.getAlertnessLevel());
            outMap.put("diastolicBloodPressure",record.getDiastolicBloodPressure());
            outMap.put("pulse",record.getPulse());
            outMap.put("pulseOxO2aturation(",record.getPulseOxO2aturation());
            outMap.put("painScale", record.getPainScale());
            outMap.put("respiratoryRate", record.getRespiratoryRate());
            outMap.put("systolicBloodPressure", record.getSystolicBloodPressure());
            return outMap;
        }).toList();
    }

    private Map<String, Object> toSerializableObject(TreatmentCirculatoryTourniquetRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("extremity", record.getExtremity());
        outMap.put("extremityType", record.getExtremityType());
        outMap.put("junctional", record.getJunctional());
        outMap.put("junctionalType", record.getJunctionalType());
        outMap.put("truncal", record.getTruncal());
        outMap.put("truncalType", record.getTruncalType());
        return outMap;
    }

    private Map<String, Object> toSerializableObject(TreatmentCirculatoryDressingRecord record) {
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("hemostatic", record.getHemostatic());
        outMap.put("other", record.getOther());
        outMap.put("otherType", record.getOtherType());
        outMap.put("pressure", record.getPressure());
        return outMap;
    }

    private Map<String, Object> toSerializableObject(TreatmentAirwayRecord record) {
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("cricothyroidotomy", record.getCricothyroidotomy());
        outMap.put("endotrachealTube", record.getEndotrachealTube());
        outMap.put("intact", record.getIntact());
        outMap.put("nasopharyngealAirway", record.getNasopharyngealAirway());
        outMap.put("supraglotticAirway", record.getSupraglotticAirway());
        outMap.put("type",record.getType());
        return outMap;
    }

    private Map<String, Object> toSerializableObject(TreatmentBreathingRecord record) {
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("chestSeal", record.getChestSeal());
        outMap.put("chestTube", record.getChestTube());
        outMap.put("needleDecompression", record.getNeedleDecompression());
        outMap.put("oxygenAdministered", record.getOxygenAdministered());
        outMap.put("type", record.getType());
        return outMap;
    }

    private List<HashMap<String, Object>> toSerializableObject(TreatmentFluidRecord[] records){
        return Arrays.stream(records).map((record)-> {
            HashMap<String, Object> outMap = new HashMap<>();
            outMap.put("name", record.getName());
            outMap.put("time", record.getTime());
            outMap.put("route", record.getRoute());
            outMap.put("volume", record.getVolume());
            return outMap;
        }).toList();
    }

    private List<HashMap<String, Object>> toSerializableObject(TreatmentMedicationsRecord[] records){
        return Arrays.stream(records).map((record)-> {
            HashMap<String, Object> outMap = new HashMap<>();
            outMap.put("name", record.getName());
            outMap.put("time", record.getTime());
            outMap.put("route", record.getRoute());
            outMap.put("dosage", record.getDosage());
            return outMap;
        }).toList();
    }

    private Map<String, Object> toSerializableObject(TreatmentOtherRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("combatPill", record.getCombatPill());
        outMap.put("eyeShieldLeft", record.getEyeShieldLeft());
        outMap.put("eyeShieldRight", record.getEyeShieldRight());
        outMap.put("hypothermiaBlanket", record.getHypothermiaBlanket());
        outMap.put("splint", record.getSplint());
        return outMap;
    }


    private Map<String, Object> toSerializableObject(ResponderRecord record){
        HashMap<String, Object> outMap = new HashMap<>();
        outMap.put("firstName", record.getFirstName());
        outMap.put("lastName", record.getLastName());
        outMap.put("socialSecurityAccountNumber", record.getSocialSecurityAccountNumber());
        outMap.put("trainingLevel", record.getTrainingLevel());
        return outMap;
    }


    @Override
    public void sendToHla(WebItem webItem) {
        try {
            HlaTacticalCombatCasualtyCareCard hlaCard = this.tacticalCombatCasualtyCareCardManager.getTacticalCombatCasualtyCareCardByHlaInstanceName(webItem.getInstanceName());
            if (hlaCard == null) {
                logger.warn("Instance not found for name: {}. Creating a new instance.", webItem.getInstanceName());
                sendNewItemToHla(webItem);
                return;
            }
            HlaTacticalCombatCasualtyCareCardUpdater updater = hlaCard.getHlaTacticalCombatCasualtyCareCardUpdater();
            updateAndSend(webItem, updater);
        } catch (Exception e) {
            logger.error("Error sending data to HLA for WebItem: {}", webItem.getInstanceName(), e);
        }
    }

    @Override
    public String sendNewItemToHla(WebItem webItem) {
        try {
            HlaTacticalCombatCasualtyCareCard newHlaCard = this.tacticalCombatCasualtyCareCardManager.createLocalHlaTacticalCombatCasualtyCareCard();
            HlaTacticalCombatCasualtyCareCardUpdater updater = newHlaCard.getHlaTacticalCombatCasualtyCareCardUpdater();
            updateAndSend(webItem, updater);
            logger.info("Created new HLA instance with name: {}", newHlaCard.getHlaInstanceName());
            return newHlaCard.getHlaInstanceName();
        } catch (HlaNotConnectedException | HlaInternalException | HlaRtiException e) {
            logger.error("Error creating new HLA instance for WebItem: {}", webItem.getInstanceName(), e);
            throw new RuntimeException(e);
        }
    }

    private void updateAndSend(WebItem webItem, HlaTacticalCombatCasualtyCareCardUpdater updater) {
        try {
            webItem.getMap().forEach((key, value) -> {
                HlaTacticalCombatCasualtyCareCardAttributes.Attribute attribute = HlaTacticalCombatCasualtyCareCardAttributes.Attribute.find(key);
                if (attribute != null && value != null) {
                    try {
                        switch (attribute) {
                            case PATIENT_ID -> updater.setPatientId((String) value);
                            case BATTLE_ROSTER_NUMBER -> updater.setBattleRosterNumber((String) value);
                            case EVACUATION_LEVEL_REQUEST -> updater.setEvacuationLevelRequest(toEvacuationCategoryEnum(value));
                            case INJURY_ANNOTATION -> updater.setInjuryAnnotation(toInjuryAnnotationRecord(value));
                            case LAST_NAME -> updater.setLastName((String) value);
                            case FIRST_NAME -> updater.setFirstName((String) value);
                            case SOCIAL_SECURITY_ACCOUNT_NUMBER -> updater.setSocialSecurityAccountNumber((String) value);
                            case GENDER -> updater.setGender((GenderEnum) value);
                            case DATE -> updater.setDate((String) value);
                            case TIME -> updater.setTime((String) value);
                            case SERVICE -> updater.setService((String) value);
                            case UNIT -> updater.setUnit((String) value);
                            case ALLERGIES -> updater.setAllergies((String) value);
                            case MECHANISM_OF_INJURY -> updater.setMechanismOfInjury((MechanismOfInjuryCommsRecord) value);
                            case SIGNS_SYMPTOMS -> updater.setSignsSymptoms((SignsSymptomsRecord[]) value);
                            case TREATMENT_CIRCULATORY_TOURNIQUET -> updater.setTreatmentCirculatoryTourniquet((TreatmentCirculatoryTourniquetRecord) value);
                            case TREATMENT_CIRCULATORY_DRESSING -> updater.setTreatmentCirculatoryDressing((TreatmentCirculatoryDressingRecord) value);
                            case TREATMENT_AIRWAY -> updater.setTreatmentAirway((TreatmentAirwayRecord) value);
                            case TREATMENT_BREATHING -> updater.setTreatmentBreathing((TreatmentBreathingRecord) value);
                            case TREATMENT_FLUIDS -> updater.setTreatmentFluids((TreatmentFluidRecord[]) value);
                            case TREATMENT_BLOOD_PRODUCTS -> updater.setTreatmentBloodProducts((TreatmentFluidRecord[]) value);
                            case TREATMENT_MEDICATIONS_ANALGESIC -> updater.setTreatmentMedicationsAnalgesic((TreatmentMedicationsRecord[]) value);
                            case TREATMENT_MEDICATIONS_ANTIBIOTIC -> updater.setTreatmentMedicationsAntibiotic((TreatmentMedicationsRecord[]) value);
                            case TREATMENT_MEDICATIONS_OTHER -> updater.setTreatmentMedicationsOther((TreatmentMedicationsRecord[]) value);
                            case TREATMENT_OTHER -> updater.setTreatmentOther((TreatmentOtherRecord) value);
                            case TREATMENT_NOTES -> updater.setTreatmentNotes((String) value);
                            case RESPONDER -> updater.setResponder((ResponderRecord) value);
                            default -> logger.warn("Unhandled attribute: {}. Skipping.", key);
                        }
                    } catch (ClassCastException e) {
                        logger.warn("Type mismatch for attribute: {} with value: {}. Skipping.", key, value, e);
                    }
                } else {
                    logger.warn("Null or unrecognized attribute key: {}. Skipping.", key);
                }
            });
            updater.sendUpdate();
            logger.info("Successfully sent update for instance: {}", webItem.getInstanceName());
        } catch (HlaNotConnectedException | HlaAttributeNotOwnedException | HlaInternalException | HlaRtiException e) {
            logger.error("Failed to send update to HLA for instance: {}", webItem.getInstanceName(), e);
            throw new RuntimeException(e);
        }
    }

    private EvacuationCategoryEnum toEvacuationCategoryEnum(Object value) {
        try {
            return value == null ? null : EvacuationCategoryEnum.valueOf(value.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid EvacuationCategoryEnum value: {}. Defaulting to UNKNOWN.", value);
            return null;
        }
    }

    private InjuryAnnotationRecord toInjuryAnnotationRecord(Object value) {
        if (!(value instanceof Map)) {
            logger.warn("Invalid value for InjuryAnnotationRecord: {}", value);
            return null;
        }

        Map<?, ?> valueMap = (Map<?, ?>) value;

        try {
            // Extract fields from the map
            TourniquetRecord rightArmTourniquet = (TourniquetRecord) valueMap.get("rightArmTourniquet");
            TourniquetRecord leftArmTourniquet = (TourniquetRecord) valueMap.get("leftArmTourniquet");
            TourniquetRecord rightLegTourniquet = (TourniquetRecord) valueMap.get("rightLegTourniquet");
            TourniquetRecord leftLegTourniquet = (TourniquetRecord) valueMap.get("leftLegTourniquet");
            InjuryLocationRecord[] annotationList = (InjuryLocationRecord[]) valueMap.get("annotationList");

            // Ensure none of the required parameters are null
            if (rightArmTourniquet == null || leftArmTourniquet == null || rightLegTourniquet == null || leftLegTourniquet == null || annotationList == null) {
                logger.error("Missing required fields for InjuryAnnotationRecord: {}", valueMap);
                return null;
            }

            // Create the InjuryAnnotationRecord
            return InjuryAnnotationRecord.create(rightArmTourniquet, leftArmTourniquet, rightLegTourniquet, leftLegTourniquet, annotationList);
        } catch (ClassCastException e) {
            logger.error("Type mismatch while creating InjuryAnnotationRecord: {}", value, e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while creating InjuryAnnotationRecord: {}", value, e);
            return null;
        }
    }


}
