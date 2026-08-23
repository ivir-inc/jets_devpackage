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

package com.ivir.devpackage.api.fed;

public class FederateInfo {
    private String name;
    private String host;
    private Integer roLength;
    private Integer reflectionsReceived;
    private Integer updatesSent;
    private Integer objectInstancesUpdated;
    private Integer objectInstancesReflected;
    private Integer objectInstancesRegistered;
    private Integer objectInstancesDiscovered;

    public String getName() {
        return name;
    }

    public FederateInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getHost() {
        return host;
    }

    public FederateInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getRoLength() {
        return roLength;
    }

    public FederateInfo setRoLength(Integer roLength) {
        this.roLength = roLength;
        return this;
    }

    public Integer getReflectionsReceived() {
        return reflectionsReceived;
    }

    public FederateInfo setReflectionsReceived(Integer reflectionsReceived) {
        this.reflectionsReceived = reflectionsReceived;
        return this;
    }

    public Integer getUpdatesSent() {
        return updatesSent;
    }

    public FederateInfo setUpdatesSent(Integer updatesSent) {
        this.updatesSent = updatesSent;
        return this;
    }

    public Integer getObjectInstancesUpdated() {
        return objectInstancesUpdated;
    }

    public FederateInfo setObjectInstancesUpdated(Integer objectInstancesUpdated) {
        this.objectInstancesUpdated = objectInstancesUpdated;
        return this;
    }

    public Integer getObjectInstancesReflected() {
        return objectInstancesReflected;
    }

    public FederateInfo setObjectInstancesReflected(Integer objectInstancesReflected) {
        this.objectInstancesReflected = objectInstancesReflected;
        return this;
    }

    public Integer getObjectInstancesRegistered() {
        return objectInstancesRegistered;
    }

    public FederateInfo setObjectInstancesRegistered(Integer objectInstancesRegistered) {
        this.objectInstancesRegistered = objectInstancesRegistered;
        return this;
    }

    public Integer getObjectInstancesDiscovered() {
        return objectInstancesDiscovered;
    }

    public FederateInfo setObjectInstancesDiscovered(Integer objectInstancesDiscovered) {
        this.objectInstancesDiscovered = objectInstancesDiscovered;
        return this;
    }
}
