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

import com.ivir.devpackage.api.fed.FederateInfo;
import com.ivir.devpackage.api.fed.MmsFederateService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class FederationDataFetcher {
    @Autowired
    MmsFederateService mmsFederateService;

    @DgsMutation
    public String joinFederation(String federateName){
        mmsFederateService.connect(federateName);
        return "connected";
    }

    @DgsMutation
    public String resignFromFederation(){
        mmsFederateService.disconnect();
        return "disconnected";
    }

    @DgsQuery
    public List<FederateInfo> federateAll(){
        return mmsFederateService.getFederates();
    }

    @DgsQuery
    public String connectionStatus(){
        return "Connected: " + mmsFederateService.isConnected();
    }

}
