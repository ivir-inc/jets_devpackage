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

package com.ivir.devpackage.controller.query.types;

public class VitalSignsInput {
    private String patientId;
    private Integer heartRate;
    private Integer diastolicBloodPressure;
    private Integer systolicBloodPressure;
    private Float peripheralOxygenSaturation;
    private Float temperatureFahrenheit;
    private Float respirationEndTidalCarbonDioxide;
    private Float respirationRate;

    public String getPatientId() {
        return patientId;
    }

    public VitalSignsInput setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public VitalSignsInput setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
        return this;
    }

    public Integer getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public VitalSignsInput setDiastolicBloodPressure(Integer diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
        return this;
    }

    public Integer getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public VitalSignsInput setSystolicBloodPressure(Integer systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
        return this;
    }

    public Float getPeripheralOxygenSaturation() {
        return peripheralOxygenSaturation;
    }

    public VitalSignsInput setPeripheralOxygenSaturation(Float peripheralOxygenSaturation) {
        this.peripheralOxygenSaturation = peripheralOxygenSaturation;
        return this;
    }

    public Float getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public VitalSignsInput setTemperatureFahrenheit(Float temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
        return this;
    }

    public Float getRespirationEndTidalCarbonDioxide() {
        return respirationEndTidalCarbonDioxide;
    }

    public VitalSignsInput setRespirationEndTidalCarbonDioxide(Float respirationEndTidalCarbonDioxide) {
        this.respirationEndTidalCarbonDioxide = respirationEndTidalCarbonDioxide;
        return this;
    }

    public Float getRespirationRate() {
        return respirationRate;
    }

    public VitalSignsInput setRespirationRate(Float respirationRate) {
        this.respirationRate = respirationRate;
        return this;
    }
}
