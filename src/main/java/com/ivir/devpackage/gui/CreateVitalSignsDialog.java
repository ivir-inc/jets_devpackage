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

package com.ivir.devpackage.gui;

import com.ivir.devpackage.controller.ControlWebClient;
import com.ivir.devpackage.controller.query.types.VitalSignsInput;
import com.ivir.devpackage.gui.builder.BoxPaneBuilder;
import com.ivir.devpackage.gui.builder.PushButtonBuilder;
import com.ivir.devpackage.gui.builder.TextInputBuilder;
import devstudio.generatedcode.datatypes.EventTypeEnum;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.*;

import java.util.concurrent.ExecutionException;

public class CreateVitalSignsDialog extends Dialog {
    private ControlWebClient controlWebClient;
    private DevPackageMainPage gui;

    private TextInput patientId;
    private TextInput heartRate;
    private TextInput diastolicBloodPressure;
    private TextInput systolicBloodPressure;
    private TextInput peripheralOxygenSaturation;
    private TextInput temperatureFahrenheit;
    private TextInput respirationEndTidalCarbonDioxide;
    private TextInput respirationRate;

    public CreateVitalSignsDialog(ControlWebClient controlWebClient, DevPackageMainPage gui) {
        this.controlWebClient = controlWebClient;
        this.gui = gui;
        this.setModal(true);
        this.setTitle("Create Vital Signs");

        Form form = new Form();
        Form.Section section = new Form.Section();

        patientId = TextInputBuilder.create().prompt("Patient ID").build();
        section.add(patientId);
        heartRate = TextInputBuilder.create().prompt("Heart Rate").build();
        section.add(heartRate);
        diastolicBloodPressure = TextInputBuilder.create().prompt("Diastolic Blood Pressure").build();
        section.add(diastolicBloodPressure);
        systolicBloodPressure = TextInputBuilder.create().prompt("Systolic Blood Pressure").build();
        section.add(systolicBloodPressure);
        peripheralOxygenSaturation = TextInputBuilder.create().prompt("O2 Saturation").build();
        section.add(peripheralOxygenSaturation);
        temperatureFahrenheit = TextInputBuilder.create().prompt("Temp (F)").build();
        section.add(temperatureFahrenheit);
        respirationEndTidalCarbonDioxide = TextInputBuilder.create().prompt("End Tidal CO2").build();
        section.add(respirationEndTidalCarbonDioxide);
        respirationRate = TextInputBuilder.create().prompt("Respiration Rate").build();
        section.add(respirationRate);

        form.getSections().add(section);

        this.setContent(BoxPaneBuilder.create()
                .orientation(Orientation.VERTICAL)
                .horizontalAlignment(HorizontalAlignment.RIGHT)
                .add(form)
                .add(BoxPaneBuilder.create()
                        .add(PushButtonBuilder.create()
                                .text("Send")
                                .onPressed((button) -> {
                                    try {
                                        gui.setQueryText(controlWebClient.mutateCreateVitalSigns(buildVitalSignsInput()).get().toString());
                                    } catch (InterruptedException | ExecutionException e) {
                                        throw new RuntimeException(e);
                                    }
                                    this.close(true);
                                })
                                .build())
                        .add(PushButtonBuilder.create()
                                .text("Cancel")
                                .onPressed((button) -> this.close(true))
                                .build())
                        .add(PushButtonBuilder.create()
                                .text("Auto-Fill")
                                .onPressed(button -> autofill())
                                .build())
                        .build())
                .build());
    }

    private void autofill() {
        this.heartRate.setText("75");
        this.systolicBloodPressure.setText("120");
        this.diastolicBloodPressure.setText("80");
        this.peripheralOxygenSaturation.setText("99");
        this.temperatureFahrenheit.setText("98.6");
        this.respirationEndTidalCarbonDioxide.setText("35");
        this.respirationRate.setText("12");
    }

    private VitalSignsInput buildVitalSignsInput() {
        VitalSignsInput vitalSignsInput = new VitalSignsInput();

        if (notNullOrEmpty(patientId.getText())) {
            vitalSignsInput.setPatientId(patientId.getText());
        }
        if (notNullOrEmpty(heartRate.getText())) {
            vitalSignsInput.setHeartRate(Integer.parseInt(heartRate.getText()));
        }
        if (notNullOrEmpty(diastolicBloodPressure.getText())) {
            vitalSignsInput.setDiastolicBloodPressure(Integer.parseInt(diastolicBloodPressure.getText()));
        }
        if (notNullOrEmpty(systolicBloodPressure.getText())) {
            vitalSignsInput.setSystolicBloodPressure(Integer.parseInt(systolicBloodPressure.getText()));
        }
        if (notNullOrEmpty(peripheralOxygenSaturation.getText())) {
            vitalSignsInput.setPeripheralOxygenSaturation(Float.parseFloat(peripheralOxygenSaturation.getText()));
        }
        if (notNullOrEmpty(temperatureFahrenheit.getText())) {
            vitalSignsInput.setTemperatureFahrenheit(Float.parseFloat(temperatureFahrenheit.getText()));
        }
        if (notNullOrEmpty(respirationEndTidalCarbonDioxide.getText())) {
            vitalSignsInput.setRespirationEndTidalCarbonDioxide(Float.parseFloat(respirationEndTidalCarbonDioxide.getText()));
        }
        if (notNullOrEmpty(respirationRate.getText())) {
            vitalSignsInput.setRespirationRate(Float.parseFloat(respirationRate.getText()));
        }

        return vitalSignsInput;
    }

    private boolean notNullOrEmpty(String textStr) {
        return textStr != null && !textStr.isEmpty();
    }
}
