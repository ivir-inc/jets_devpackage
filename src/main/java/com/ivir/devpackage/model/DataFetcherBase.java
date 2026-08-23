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

import com.ivir.devpackage.api.fed.MmsFederateService;
import com.ivir.devpackage.api.model.WebItem;
import com.ivir.devpackage.api.model.WebItemToWebCallback;
import com.netflix.graphql.dgs.DgsComponent;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DgsComponent
public class DataFetcherBase {

    private Storage storage;

    @Autowired
    StorageService storageService;

    @Autowired
    MmsFederateService mmsFederateService;

    protected void initialize(String storageName){
        this.storage = storageService.getStorage(storageName);
    }

    protected void initialize(String storageName, String ... newIndexes){
        this.storage = storageService.getStorage(storageName);
        this.storage.createIndex(newIndexes);
    }

    protected Map<String, Object> getById(Integer itemId){
        assertFederationIsConnected();
        Optional<WebItem> webItemOptional = storage.getItemById(itemId);
        if(webItemOptional.isEmpty()){
            return null;
        }
        return webItemOptional.get().getMap();
    }

    protected List<Map<String, Object>> getAll(){
        assertFederationIsConnected();
        return storage.getAllItems().stream().map((webItem) -> webItem.getMap()).toList();
    }

    protected List<Map<String, Object>> getByField(String fieldName, Object value){
        assertFederationIsConnected();
        return storage.getByField(fieldName, value).stream().map((webItem) -> webItem.getMap()).toList();
    }

    protected String update(DataFetchingEnvironment dfe, String varName){
        assertFederationIsConnected();
        Map<String, Object> dataMap = (Map<String,Object>)dfe.getArguments().get(varName);
        storage.updateFromWeb(new WebItem(dataMap,true));
        return "done";
    }

    protected Publisher<Map<String,Object>> monitor() {
        assertFederationIsConnected();
        return Flux.create((sink)->{
            storage.addWebItemToWebCallback(new WebItemToWebCallback() {
                @Override
                public void sendToWeb(WebItem webItem) {
                    sink.next(webItem.getMap());
                }
            });
        });
    }

    protected String release(String instanceName){
        assertFederationIsConnected();
        storage.requestRelease(instanceName);
        return "done";
    }

    protected String acquire(String instanceName){
        assertFederationIsConnected();
        storage.requestAcquire(instanceName);
        return "done";
    }

    private void assertFederationIsConnected(){
        if(!mmsFederateService.isConnected()){
            throw new RuntimeException("You must connect to the federation first");
        }
    }





}
