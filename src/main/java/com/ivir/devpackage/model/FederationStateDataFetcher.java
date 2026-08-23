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
public class FederationStateDataFetcher extends DataFetcherBase{
    public static Logger logger = LoggerFactory.getLogger(FederationStateDataFetcher.class);

    @PostConstruct
    public void init(){
        initialize("FederationState");
    }

    @DgsQuery
    public Map<String, Object> federationStateById(@InputArgument Integer itemId) {
        return getById(itemId);
    }

    @DgsQuery
    public List<Map<String, Object>> federationStateAll(){
        return getAll();
    }

    @DgsData(parentType = "Mutation", field = "federationStateUpdate")
    public String federationStateUpdate(DataFetchingEnvironment dfe){
        logger.info("mutation: federationStateUpdate called");
        return update(dfe, "federationState");    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorFederationState() {
        return monitor();
    }
}
