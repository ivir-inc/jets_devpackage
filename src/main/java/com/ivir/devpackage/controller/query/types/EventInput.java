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

import devstudio.generatedcode.datatypes.EventTypeEnum;

public class EventInput {
    private Long time;
    private Long simTime;
    private EventTypeEnum type;
    private String source;
    private String patientId;
    private String learnerId;
    private String instructorId;
    private String teamId;
    private String trainingFacilityId;
    private String notes;
    private String description;

    public Long getTime() {
        return time;
    }

    public EventInput setTime(Long time) {
        this.time = time;
        return this;
    }

    public Long getSimTime() {
        return simTime;
    }

    public EventInput setSimTime(Long simTime) {
        this.simTime = simTime;
        return this;
    }

    public EventTypeEnum getType() {
        return type;
    }

    public EventInput setType(EventTypeEnum type) {
        this.type = type;
        return this;
    }

    public String getSource() {
        return source;
    }

    public EventInput setSource(String source) {
        this.source = source;
        return this;
    }

    public String getPatientId() {
        return patientId;
    }

    public EventInput setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public EventInput setLearnerId(String learnerId) {
        this.learnerId = learnerId;
        return this;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public EventInput setInstructorId(String instructorId) {
        this.instructorId = instructorId;
        return this;
    }

    public String getTeamId() {
        return teamId;
    }

    public EventInput setTeamId(String teamId) {
        this.teamId = teamId;
        return this;
    }

    public String getTrainingFacilityId() {
        return trainingFacilityId;
    }

    public EventInput setTrainingFacilityId(String trainingFacilityId) {
        this.trainingFacilityId = trainingFacilityId;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public EventInput setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventInput setDescription(String description) {
        this.description = description;
        return this;
    }
}
