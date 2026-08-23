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

public class PhysiologyInput { 
    private String patientId;
    private Integer heartRate;
    private Integer diastolicBloodPressure;
    private Integer systolicBloodPressure;
    private Float peripheralOxygenSaturation;
    private Float temperatureFahrenheit;
    private Float respirationEndTidalCarbonDioxide;
    private Float respirationRate;
    private Integer lungTidalVolume;
    private Integer lungDeadSpace;
    private Integer lungTotalCapacity;
    private Integer lungExpiratoryReserve;
    private Integer lungInspiratoryReserve;
    private Integer lungResidualVolume;

    public String getPatientId() {
        return patientId;
    }

    public PhysiologyInput setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public PhysiologyInput setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
        return this;
    }

    public Integer getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public PhysiologyInput setDiastolicBloodPressure(Integer diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
        return this;
    }

    public Integer getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public PhysiologyInput setSystolicBloodPressure(Integer systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
        return this;
    }

    public Float getPeripheralOxygenSaturation() {
        return peripheralOxygenSaturation;
    }

    public PhysiologyInput setPeripheralOxygenSaturation(Float peripheralOxygenSaturation) {
        this.peripheralOxygenSaturation = peripheralOxygenSaturation;
        return this;
    }

    public Float getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public PhysiologyInput setTemperatureFahrenheit(Float temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
        return this;
    }

    public Float getRespirationEndTidalCarbonDioxide() {
        return respirationEndTidalCarbonDioxide;
    }

    public PhysiologyInput setRespirationEndTidalCarbonDioxide(Float respirationEndTidalCarbonDioxide) {
        this.respirationEndTidalCarbonDioxide = respirationEndTidalCarbonDioxide;
        return this;
    }

    public Float getRespirationRate() {
        return respirationRate;
    }

    public PhysiologyInput setRespirationRate(Float respirationRate) {
        this.respirationRate = respirationRate;
        return this;
    }

    public Integer getLungTidalVolume() {
        return lungTidalVolume;
    }

    public PhysiologyInput setLungTidalVolume(Integer lungTidalVolume) {
        this.lungTidalVolume = lungTidalVolume;
        return this;
    }

    public Integer getLungDeadSpace() {
        return lungDeadSpace;
    }

    public PhysiologyInput setLungDeadSpace(Integer lungDeadSpace) {
        this.lungDeadSpace = lungDeadSpace;
        return this;
    }

    public Integer getLungTotalCapacity() {
        return lungTotalCapacity;
    }

    public PhysiologyInput setLungTotalCapacity(Integer lungTotalCapacity) {
        this.lungTotalCapacity = lungTotalCapacity;
        return this;
    }

    public Integer getLungExpiratoryReserve() {
        return lungExpiratoryReserve;
    }

    public PhysiologyInput setLungExpiratoryReserve(Integer lungExpiratoryReserve) {
        this.lungExpiratoryReserve = lungExpiratoryReserve;
        return this;
    }

    public Integer getLungInspiratoryReserve() {
        return lungInspiratoryReserve;
    }

    public PhysiologyInput setLungInspiratoryReserve(Integer lungInspiratoryReserve) {
        this.lungInspiratoryReserve = lungInspiratoryReserve;
        return this;
    }

    public Integer getLungResidualVolume() {
        return lungResidualVolume;
    }

    public PhysiologyInput setLungResidualVolume(Integer lungResidualVolume) {
        this.lungResidualVolume = lungResidualVolume;
        return this;
    }
}
