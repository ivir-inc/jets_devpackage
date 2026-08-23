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

package com.ivir.devpackage.tester;

import devstudio.generatedcode.*;
import devstudio.generatedcode.datatypes.EventTypeEnum;
import devstudio.generatedcode.exceptions.HlaAttributeNotOwnedException;
import devstudio.generatedcode.exceptions.HlaInternalException;
import devstudio.generatedcode.exceptions.HlaNotConnectedException;
import devstudio.generatedcode.exceptions.HlaRtiException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class TesterFederate {
    private HlaWorld hlaWorld;
    private boolean connected = false;

    private HlaEventManager eventManager;
    private int eventCounter = 0;
    private HlaVitalSignsManager vitalSignsManager;
    private int physiologyCounter = 0;

    public HlaWorld getHlaWorld() {
        return hlaWorld;
    }

    public void connect(String fedName){
        if(fedName == null){
            fedName = "JDS";
        }
        System.setProperty("devstudio.generatedcode.federateName",fedName);

        hlaWorld = HlaWorld.Factory.create();

        try {
            HlaLogicalTime logicalTime = hlaWorld.connect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not connect to the federation", e);
        }

        eventManager = hlaWorld.getHlaEventManager();
        vitalSignsManager = hlaWorld.getHlaVitalSignsManager();


//        Storage storage = storageService.getStorage("Physiology");
//        HlaVitalSignsManager physiologyManager = hlaWorld.getHlaPhysiologyManager();
//        physiologyManager.addHlaVitalSignsDefaultInstanceListener(new PhysiologyFedListener(storage, physiologyManager));
//
//        storage = storageService.getStorage("BloodGasLab");
//        HlaBloodGasLabManager bloodGasLabManager = hlaWorld.getHlaBloodGasLabManager();
//        bloodGasLabManager.addHlaBloodGasLabDefaultInstanceListener(new BloodGasLabFedListener(storage, bloodGasLabManager));
//
//        storage = storageService.getStorage("BloodLab");
//        HlaBloodLabManager bloodLabManager = hlaWorld.getHlaBloodLabManager();
//        bloodLabManager.addHlaBloodLabDefaultInstanceListener(new BloodLabFedListener(storage, bloodLabManager));
//
//        storage = storageService.getStorage("BurnInjury");
//        HlaBurnInjuryManager burnInjuryManager = hlaWorld.getHlaBurnInjuryManager();
//        burnInjuryManager.addHlaBurnInjuryDefaultInstanceListener(new BurnInjuryFedListener(storage, burnInjuryManager));
//
//        storage = storageService.getStorage("CreatePatient");
//        HlaCreatePatientManager createPatientManager = hlaWorld.getHlaCreatePatientManager();
//        createPatientManager.addHlaCreatePatientDefaultInstanceListener(new CreatePatientFedListener(storage, createPatientManager));
//
//        storage = storageService.getStorage("DataLog");
//        HlaDataLogManager dataLogManager = hlaWorld.getHlaDataLogManager();
//        dataLogManager.addHlaDataLogDefaultInstanceListener(new DataLogFedListener(storage, dataLogManager));
//
//        storage = storageService.getStorage("DateTime");
//        HlaDateTimeManager dateTimeManager = hlaWorld.getHlaDateTimeManager();
//        dateTimeManager.addHlaDateTimeDefaultInstanceListener(new DateTimeFedListener(storage, dateTimeManager));
//
//        storage = storageService.getStorage("Document");
//        HlaDocumentManager documentManager = hlaWorld.getHlaDocumentManager();
//        documentManager.addHlaDocumentDefaultInstanceListener(new DocumentFedListener(storage, documentManager));
//
//        storage = storageService.getStorage("Event");
//        HlaEventManager eventManager = hlaWorld.getHlaEventManager();
//        eventManager.addHlaEventDefaultInstanceListener(new EventFedListener(storage, eventManager));
//
//        storage = storageService.getStorage("FederateData");
//        HlaFederateDataManager federateDataManager = hlaWorld.getHlaFederateDataManager();
//        federateDataManager.addHlaFederateDataDefaultInstanceListener(new FederateDataFedListener(storage, federateDataManager));
//
//        storage = storageService.getStorage("FederationState");
//        HlaFederationStateManager federationStateManager = hlaWorld.getHlaFederationStateManager();
//        federationStateManager.addHlaFederationStateDefaultInstanceListener(new FederationStateFedListener(storage, federationStateManager));
//
//        storage = storageService.getStorage("Injury");
//        HlaInjuryManager injuryManager = hlaWorld.getHlaInjuryManager();
//        injuryManager.addHlaInjuryDefaultInstanceListener(new InjuryFedListener(storage, injuryManager));
//
//        storage = storageService.getStorage("MedicationTreatment");
//        HlaMedicationTreatmentManager medicationTreatmentManager = hlaWorld.getHlaMedicationTreatmentManager();
//        medicationTreatmentManager.addHlaMedicationTreatmentDefaultInstanceListener(new MedicationTreatmentFedListener(storage, medicationTreatmentManager));
//
//        storage = storageService.getStorage("PhysicalTreatment");
//        HlaPhysicalTreatmentManager physicalTreatmentManager = hlaWorld.getHlaPhysicalTreatmentManager();
//        physicalTreatmentManager.addHlaPhysicalTreatmentDefaultInstanceListener(new PhysicalTreatmentFedListener(storage, physicalTreatmentManager));
//
//        storage = storageService.getStorage("TacticalCombatCasualtyCareCard");
//        HlaTacticalCombatCasualtyCareCardManager tacticalCombatCasualtyCareCardManager = hlaWorld.getHlaTacticalCombatCasualtyCareCardManager();
//        tacticalCombatCasualtyCareCardManager.addHlaTacticalCombatCasualtyCareCardDefaultInstanceListener(new TacticalCombatCasualtyCareCardFedListener(storage, tacticalCombatCasualtyCareCardManager));
//
//        storage = storageService.getStorage("Treatment");
//        HlaTreatmentManager treatmentManager = hlaWorld.getHlaTreatmentManager();
//        treatmentManager.addHlaTreatmentDefaultInstanceListener(new TreatmentFedListener(storage, treatmentManager));
//
//        storage = storageService.getStorage("UrineLab");
//        HlaUrineLabManager urineLabManager = hlaWorld.getHlaUrineLabManager();
//        urineLabManager.addHlaUrineLabDefaultInstanceListener(new UrineLabFedListener(storage, urineLabManager));
//
//        HlaInteractionManager interactionManager = hlaWorld.getHlaInteractionManager();
//        interactionManager.addHlaInteractionListener(new InteractionFedListener(storageService, interactionManager));
//
//        storage = storageService.getStorage("BodyFluids");
//        HlaBodyFluidsManager bodyFluidsManager = hlaWorld.getHlaBodyFluidsManager();
//        bodyFluidsManager.addHlaBodyFluidsDefaultInstanceListener(new BodyFluidsFedListener(storage, bodyFluidsManager));
//
//        storage = storageService.getStorage("NeurologicalScales");
//        HlaNeurologicalScalesManager neurologicalScalesManager = hlaWorld.getHlaNeurologicalScalesManager();
//        neurologicalScalesManager.addHlaNeurologicalScalesDefaultInstanceListener(new NeurologicalScalesFedListener(storage, neurologicalScalesManager));

        connected = true;
    }

    public void createEvents(int count){
        for(int next = 0; next < count; next ++){
            try {
                eventCounter ++;
                HlaEvent event = eventManager.createLocalHlaEvent();
                HlaEventUpdater eventUpdater = event.getHlaEventUpdater();
                eventUpdater.setDescription("Event created by tester #" + eventCounter);
                eventUpdater.setInstructorId("Instructor" + eventCounter);
                eventUpdater.setLearnerId("Learner" + eventCounter);
                eventUpdater.setNotes("This is a note for event #" + eventCounter);
                eventUpdater.setPatientId("Patient1");
                eventUpdater.setSimTime(new Date().getTime());
                eventUpdater.setTime(new Date().getTime());
                eventUpdater.setSource("the source");
                eventUpdater.setTrainingFacilityId("the facility");
                eventUpdater.setType(EventTypeEnum.TREATMENT);
                eventUpdater.sendUpdate();
            } catch (HlaNotConnectedException e) {
                throw new RuntimeException(e);
            } catch (HlaInternalException e) {
                throw new RuntimeException(e);
            } catch (HlaRtiException e) {
                throw new RuntimeException(e);
            } catch (HlaAttributeNotOwnedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createPhysiology(int count){
        Random random = new Random();
        for(int next = 0; next < count; next ++){
            try {
                physiologyCounter ++;
                HlaVitalSigns physiology = vitalSignsManager.createLocalHlaVitalSigns();
                HlaVitalSignsUpdater physiologyUpdater = physiology.getHlaVitalSignsUpdater();
                physiologyUpdater.setPatientId("patient1");
                physiologyUpdater.setDiastolicBloodPressure(random.nextInt(200));
                physiologyUpdater.setSystolicBloodPressure(random.nextInt(100));
                physiologyUpdater.setHeartRate(random.nextInt(100));
                physiologyUpdater.setPeripheralOxygenSaturation(random.nextInt(101));
                physiologyUpdater.setRespirationEndTidalCarbonDioxide(random.nextInt(101));
                physiologyUpdater.setRespirationRate(random.nextFloat(100));
                physiologyUpdater.setTemperatureFahrenheit(random.nextFloat(120));
                physiologyUpdater.sendUpdate();
            } catch (HlaNotConnectedException e) {
                throw new RuntimeException(e);
            } catch (HlaInternalException e) {
                throw new RuntimeException(e);
            } catch (HlaRtiException e) {
                throw new RuntimeException(e);
            } catch (HlaAttributeNotOwnedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getEvents(){
        eventManager.getAllHlaEvents().forEach((event)->{
            System.out.println("Instance Name:" + event.getHlaInstanceName());
            if(event.hasInstructorId())
                System.out.println("Instructor ID:" + event.getInstructorId());
            if(event.hasLearnerId())
                System.out.println("Learner ID:" + event.getLearnerId());
            if(event.hasPatientId())
                System.out.println("Patient ID:" + event.getPatientId());
            if(event.hasTeamId())
                System.out.println("Team ID:" + event.getTeamId());
            if(event.hasTrainingFacilityId())
                System.out.println("TrainingFacility ID:" + event.getTrainingFacilityId());
            if(event.hasType())
                System.out.println("Type:" + event.getType());
            if(event.hasSimTime())
                System.out.println("SimTime:" + event.getSimTime());
            if(event.hasTime())
                System.out.println("Time:" + event.getTime());
            if(event.hasDescription())
                System.out.println("Description:" + event.getDescription());
            if(event.hasNotes())
                System.out.println("Notes:" + event.getNotes());
            System.out.println("------------------------------------------------");
        });
    }

    public void getPhysiology(){
        vitalSignsManager.getHlaVitalSigns().forEach((physiology)->{
            System.out.println("Instance Name:" + physiology.getHlaInstanceName());
            if(physiology.hasPatientId())
                System.out.println("Patient ID:" + physiology.getPatientId());
            if(physiology.hasHeartRate())
                System.out.println("Heart Rate:" + physiology.getHeartRate());
            if(physiology.hasRespirationRate())
                System.out.println("Respiration Rate:" + physiology.getRespirationRate());
            if(physiology.hasDiastolicBloodPressure())
                System.out.println("Diastolic:" + physiology.getDiastolicBloodPressure());
            if(physiology.hasSystolicBloodPressure())
                System.out.println("Systolic:" + physiology.getSystolicBloodPressure());
            if(physiology.hasTemperatureFahrenheit())
                System.out.println("Temp:" + physiology.getTemperatureFahrenheit());
            if(physiology.hasPeripheralOxygenSaturation())
                System.out.println("O2:" + physiology.getPeripheralOxygenSaturation());
            if(physiology.hasRespirationEndTidalCarbonDioxide())
                System.out.println("End Tidal CO2:" + physiology.getRespirationEndTidalCarbonDioxide());
            System.out.println("------------------------------------------------");
        });
    }

    public void disconnect() {
        try {
            hlaWorld.disconnect();
            connected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return connected;
    }

}