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
public class UrineLabDataFetcher extends DataFetcherBase{
    public static Logger logger = LoggerFactory.getLogger(UrineLabDataFetcher.class);

    @PostConstruct
    public void init(){
        initialize("UrineLab");
    }

    @DgsQuery
    public Map<String, Object> urineLabById(@InputArgument Integer itemId) {
        return getById(itemId);
    }

    @DgsQuery
    public List<Map<String, Object>> urineLabAll(){
        return getAll();
    }

    @DgsData(parentType = "Mutation", field = "urineLabUpdate")
    public String urineLabUpdate(DataFetchingEnvironment dfe){
        logger.info("mutation: urineLabUpdate called");
        return update(dfe, "urineLab");
    }

    @DgsSubscription
    public Publisher<Map<String,Object>> monitorUrineLab() {
        return monitor();
    }
}
