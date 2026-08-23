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

import java.util.List;

import com.ivir.devpackage.fed.v2.V2PhysiologyWebItemCallbackHandler;
import devstudio.generatedcode.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivir.devpackage.api.fed.FederateInfo;
import com.ivir.devpackage.api.fed.MmsFederateService;
import com.ivir.devpackage.model.Storage;
import com.ivir.devpackage.model.StorageService;

import devstudio.generatedcode.exceptions.*;

@Service
public class MmsFederateServiceImpl implements MmsFederateService {

    @Autowired
    private StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(MmsFederateServiceImpl.class);
    private HlaWorld hlaWorld;
    private boolean connected = false;

    public HlaWorld getHlaWorld() {
        return hlaWorld;
    }

    @Override
    public void connect(String fedName) {
        if (fedName == null || fedName.isBlank()) {
            fedName = "JDS";
        }
        System.setProperty("devstudio.generatedcode.federateName", fedName);

        hlaWorld = HlaWorld.Factory.create();

        try {
            // Registering listeners with explicitly named storage and managers
            registerNamedListeners();

            hlaWorld.connect();
            logger.info("Connected to HLA Federation with name: {}", fedName);

            connected = true;
            logger.info("Listeners successfully registered.");
        } catch (Exception e) {
            logger.error("Error connecting to the federation", e);
            throw new RuntimeException("Could not connect to the federation", e);
        }
    }

    private void registerNamedListeners() {
        // Register DateTime listener
        Storage dateTimeStorage = storageService.getStorage("DateTime");
        HlaDateTimeManager dateTimeManager = hlaWorld.getHlaDateTimeManager();
        dateTimeManager.addHlaDateTimeDefaultInstanceListener(new DateTimeFedListener(dateTimeStorage, dateTimeManager));
        logger.info("Registered DateTime listener.");

        // Register Document listener
        Storage documentStorage = storageService.getStorage("Document");
        HlaDocumentManager documentManager = hlaWorld.getHlaDocumentManager();
        documentManager.addHlaDocumentDefaultInstanceListener(new DocumentFedListener(documentStorage, documentManager));
        logger.info("Registered Document listener.");

        // Register FederateData listener
        Storage federateDataStorage = storageService.getStorage("FederateData");
        HlaFederateDataManager federateDataManager = hlaWorld.getHlaFederateDataManager();
        federateDataManager.addHlaFederateDataDefaultInstanceListener(new FederateDataFedListener(federateDataStorage, federateDataManager));
        logger.info("Registered FederateData listener.");

        // Register FederationState listener
        Storage federationStateStorage = storageService.getStorage("FederationState");
        HlaFederationStateManager federationStateManager = hlaWorld.getHlaFederationStateManager();
        federationStateManager.addHlaFederationStateDefaultInstanceListener(new FederationStateFedListener(federationStateStorage, federationStateManager));
        logger.info("Registered FederationState listener.");

        // Register MedicationTreatment listener
        Storage medicationTreatmentStorage = storageService.getStorage("MedicationTreatment");
        HlaMedicationTreatmentManager medicationTreatmentManager = hlaWorld.getHlaMedicationTreatmentManager();
        medicationTreatmentManager.addHlaMedicationTreatmentDefaultInstanceListener(new MedicationTreatmentFedListener(medicationTreatmentStorage, medicationTreatmentManager));
        logger.info("Registered MedicationTreatment listener.");

        // Register PhysicalTreatment listener
        Storage physicalTreatmentStorage = storageService.getStorage("PhysicalTreatment");
        HlaPhysicalTreatmentManager physicalTreatmentManager = hlaWorld.getHlaPhysicalTreatmentManager();
        physicalTreatmentManager.addHlaPhysicalTreatmentDefaultInstanceListener(new PhysicalTreatmentFedListener(physicalTreatmentStorage, physicalTreatmentManager));
        logger.info("Registered PhysicalTreatment listener.");

        // Register TacticalCombatCasualtyCareCard listener
        Storage tacticalCombatStorage = storageService.getStorage("TacticalCombatCasualtyCareCard");
        HlaTacticalCombatCasualtyCareCardManager tacticalCombatManager = hlaWorld.getHlaTacticalCombatCasualtyCareCardManager();
        tacticalCombatManager.addHlaTacticalCombatCasualtyCareCardDefaultInstanceListener(new TacticalCombatCasualtyCareCardFedListener(tacticalCombatStorage, tacticalCombatManager));
        logger.info("Registered TacticalCombatCasualtyCareCard listener.");

        // Register BloodGasLab listener
        Storage bloodGasLabStorage = storageService.getStorage("BloodGasLab");
        HlaBloodGasLabManager bloodGasLabManager = hlaWorld.getHlaBloodGasLabManager();
        bloodGasLabManager.addHlaBloodGasLabDefaultInstanceListener(new BloodGasLabFedListener(bloodGasLabStorage, bloodGasLabManager));
        logger.info("Registered BloodGasLab listener.");

        // Register BloodGasLab listener
        Storage bloodLabStorage = storageService.getStorage("BloodLab");
        HlaBloodLabManager bloodLabManager = hlaWorld.getHlaBloodLabManager();
        bloodLabManager.addHlaBloodLabDefaultInstanceListener(new BloodLabFedListener(bloodLabStorage, bloodLabManager));
        logger.info("Registered BloodLab listener.");

        // Register UrineLab listener
        Storage urineLabStorage = storageService.getStorage("UrineLab");
        HlaUrineLabManager urineLabManager = hlaWorld.getHlaUrineLabManager();
        urineLabManager.addHlaUrineLabDefaultInstanceListener(new UrineLabFedListener(urineLabStorage, urineLabManager));
        logger.info("Registered UrineLab listener.");

        // Register BodyFluids listener
        Storage bodyFluidsStorage = storageService.getStorage("BodyFluids");
        HlaBodyFluidsManager bodyFluidsManager = hlaWorld.getHlaBodyFluidsManager();
        bodyFluidsManager.addHlaBodyFluidsDefaultInstanceListener(new BodyFluidsFedListener(bodyFluidsStorage, bodyFluidsManager));
        logger.info("Registered BodyFluids listener.");

        // Register NeurologicalScales listener
        Storage neurologicalScalesStorage = storageService.getStorage("NeurologicalScales");
        HlaNeurologicalScalesManager neurologicalScalesManager = hlaWorld.getHlaNeurologicalScalesManager();
        neurologicalScalesManager.addHlaNeurologicalScalesDefaultInstanceListener(new NeurologicalScalesFedListener(neurologicalScalesStorage, neurologicalScalesManager));
        logger.info("Registered NeurologicalScales listener.");

        // Register VitalSigns listener
        HlaVitalSignsManager vitalSignsManager = hlaWorld.getHlaVitalSignsManager();
        VitalSignsFedListener vitalSignsFedListener = new VitalSignsFedListener(storageService, vitalSignsManager);
        vitalSignsManager.addHlaVitalSignsDefaultInstanceListener(vitalSignsFedListener);
        logger.info("Registered VitalSigns listener.");

        // Register RespiratoryPhysiology listener
        HlaRespiratoryPhysiologyManager respiratoryPhysiologyManager = hlaWorld.getHlaRespiratoryPhysiologyManager();
        RespiratoryPhysiologyFedListener respiratoryPhysiologyFedListener = new RespiratoryPhysiologyFedListener(storageService, respiratoryPhysiologyManager);
        respiratoryPhysiologyManager.addHlaRespiratoryPhysiologyDefaultInstanceListener(respiratoryPhysiologyFedListener);
        logger.info("Registered RespiratoryPhysiology listener.");

        //NOTE:  this is only for v3 -> v2 backwards compatability.  once move to v4, we can delete this handler
        Storage physiologyStorage = storageService.getStorage("Physiology");
        physiologyStorage.setWebItemToHlaCallback(new V2PhysiologyWebItemCallbackHandler(vitalSignsFedListener, respiratoryPhysiologyFedListener));

        // Register Interaction listener
        HlaInteractionManager interactionManager = hlaWorld.getHlaInteractionManager();
        interactionManager.addHlaInteractionListener(new InteractionFedListener(storageService, interactionManager));
        logger.info("Registered Interaction listener.");

        // Register Event listener
        Storage eventStorage = storageService.getStorage("Event");
        HlaEventManager eventManager = hlaWorld.getHlaEventManager();
        eventManager.addHlaEventDefaultInstanceListener(new EventFedListener(eventStorage, eventManager));
        logger.info("Registered Event listener.");

        // Register Injury listener
        Storage injuryStorage = storageService.getStorage("Injury");
        HlaInjuryManager injuryManager = hlaWorld.getHlaInjuryManager();
        injuryManager.addHlaInjuryDefaultInstanceListener(new InjuryFedListener(injuryStorage, injuryManager));
        logger.info("Registered Injury listener.");

        // Register Signs listener
        Storage signsStorage = storageService.getStorage("Signs");
        HlaSignsManager signsManager = hlaWorld.getHlaSignsManager();
        signsManager.addHlaSignsDefaultInstanceListener(new SignsFedListener(signsStorage, signsManager));
        logger.info("Registered Signs listener.");

        // Register Symptoms listener
        Storage symptomsStorage = storageService.getStorage("Symptoms");
        HlaSymptomsManager symptomsManager = hlaWorld.getHlaSymptomsManager();
        symptomsManager.addHlaSymptomsDefaultInstanceListener(new SymptomsFedListener(symptomsStorage, symptomsManager));
        logger.info("Registered Symptoms listener.");

        // Register Facility listener
        Storage facilityStorage = storageService.getStorage("Facility");
        HlaFacilityManager facilityManager = hlaWorld.getHlaFacilityManager();
        facilityManager.addHlaFacilityDefaultInstanceListener(new FacilityFedListener(facilityStorage, facilityManager));
        logger.info("Registered Facility listener.");

        // Register CasualtyState listener
        Storage casualtyStateStorage = storageService.getStorage("CasualtyState");
        HlaCasualtyStateManager casualtyStateManager = hlaWorld.getHlaCasualtyStateManager();
        casualtyStateManager.addHlaCasualtyStateDefaultInstanceListener(new CasualtyStateFedListener(casualtyStateStorage, casualtyStateManager));
        logger.info("Registered CasualtyState listener.");
    }

    @Override
    public void disconnect() {
        try {
            hlaWorld.disconnect();
            connected = false;
            logger.info("Disconnected from the federation.");
        } catch (HlaFederateOwnsAttributeException | HlaInternalException | HlaRtiException e) {
            logger.error("Failed to disconnect from the federation", e);
        }
    }

    @Override
    public boolean isConnected(){
        return connected;
    }

    @Override
    public List<FederateInfo> getFederates(){
        return hlaWorld.getHlaHLAfederateManager()
                .getAllHlaHLAfederates()
                .stream()
                .map(this::federateInfoMap)
                .toList();
    }

    private FederateInfo federateInfoMap(HlaHLAfederate hlaHLAfederate) {
        return new FederateInfo()
                .setName(hlaHLAfederate.getHLAfederateName("n/a"))
                .setHost(hlaHLAfederate.getHLAfederateHost("n/a"))
                .setRoLength(hlaHLAfederate.getHLAROlength(0))
                .setObjectInstancesDiscovered(hlaHLAfederate.getHLAobjectInstancesDiscovered(0))
                .setObjectInstancesReflected(hlaHLAfederate.getHLAobjectInstancesReflected(0))
                .setObjectInstancesRegistered(hlaHLAfederate.getHLAobjectInstancesRegistered(0))
                .setReflectionsReceived(hlaHLAfederate.getHLAreflectionsReceived(0))
                .setUpdatesSent(hlaHLAfederate.getHLAupdatesSent(0))
                .setObjectInstancesUpdated(hlaHLAfederate.getHLAobjectInstancesUpdated(0));
    }
}
