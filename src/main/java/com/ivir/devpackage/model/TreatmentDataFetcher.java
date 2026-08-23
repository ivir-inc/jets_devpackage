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
public class TreatmentDataFetcher extends DataFetcherBase{
    public static Logger logger = LoggerFactory.getLogger(TreatmentDataFetcher.class);

    @PostConstruct
    public void init(){
        initialize("Treatment");
    }

    @DgsQuery
    public Map<String, Object> treatmentById(@InputArgument Integer itemId) {
        return getById(itemId);
    }

    @DgsQuery
    public List<Map<String, Object>> treatmentAll(){
        return getAll();
    }

    @DgsQuery
    public List<Map<String, Object>> treatmentByPatientId(@InputArgument String patientId) {
        logger.debug("Querying Treatment with patientId: {}", patientId);
        return getByField("patientId", patientId).stream().toList();
    }

    @DgsData(parentType = "Mutation", field = "treatmentUpdate")
    public String treatmentUpdate(DataFetchingEnvironment dfe){
        logger.info("mutation: treatmentUpdate called");
        return update(dfe, "treatment");
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorTreatment() {
        return monitor();
    }
}
