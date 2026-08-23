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

package com.ivir.devpackage.model;

import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@DgsComponent
public class VitalSignsDataFetcher extends DataFetcherBase {

    private static final Logger logger = LoggerFactory.getLogger(VitalSignsDataFetcher.class);

    @PostConstruct
    public void init() {
        initialize("VitalSigns", "patientId");
        logger.debug("VitalSignsDataFetcher initialized.");
    }

    @DgsQuery
    public Map<String, Object> vitalSignsById(@InputArgument Integer itemId) {
        logger.debug("Querying VitalSigns with itemId: {}", itemId);
        return getById(itemId);
    }

    @DgsQuery
    public List<Map<String, Object>> vitalSignsAll() {
        logger.debug("Querying all VitalSigns records.");
        return getAll();
    }

    @DgsQuery
    public Map<String, Object> vitalSignsByPatientId(@InputArgument String patientId) {
        logger.debug("Querying VitalSigns with patientId: {}", patientId);
        return getByField("patientId", patientId).stream().findFirst().orElse(null);
    }

    @DgsData(parentType = "Mutation", field = "vitalSignsUpdate")
    public String vitalSignsUpdate(DataFetchingEnvironment dfe) {
        logger.debug("Mutation: vitalSignsUpdate called.");
        return update(dfe, "vitalSigns");
    }

    @DgsData(parentType = "Mutation", field = "vitalSignsRelease")
    public String vitalSignsRelease(DataFetchingEnvironment dfe){
        return release(dfe.getArgument("instanceName").toString());
    }

    @DgsData(parentType = "Mutation", field = "vitalSignsAcquire")
    public String vitalSignsAcquire(DataFetchingEnvironment dfe){
        return acquire(dfe.getArgument("instanceName").toString());
    }

    @DgsSubscription
    public Publisher<Map<String, Object>> monitorVitalSigns() {
        logger.debug("Subscription: monitorVitalSigns called.");
        return monitor();
    }
}
