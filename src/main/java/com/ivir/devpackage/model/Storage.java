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

import com.ivir.devpackage.api.model.*;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

import static org.dizitart.no2.filters.FluentFilter.where;

public class Storage {
    public static Logger logger = LoggerFactory.getLogger(Storage.class);

    private NitriteCollection collection;
    private int itemIdCounter = 0;
    private Optional<WebItemToHlaCallback> webItemToHlaCallback = Optional.empty();
    private ArrayList<WebItemToWebCallback> webItemToWebCallbackList = new ArrayList<>();
    private ArrayList<Transformer> transformerList = new ArrayList<>();
    private String type;
    private Optional<OwnershipToHlaCallback> ownershipToHlaCallback = Optional.empty();

    public Storage(String type, NitriteCollection collection){
       this.type = type;
       this.collection = collection;
    }

    public void createIndex(String ... fieldNames){
        this.collection.createIndex(fieldNames);
    }

    /**
     *
     * @param instanceName this can be null for items like interactions
     * @param updateAction
     */
    public void updateFromHla(String instanceName, Consumer<WebItem> updateAction) {
        WebItem webItem = null;
        Document document = null;
        if(instanceName != null){
            document = collection.find(where(WebItem.INSTANCE_NAME_KEY).eq(instanceName)).firstOrNull();
        }
        if(document == null){
            logger.debug("document with instance name {} not found, creating new webitem", instanceName);
            webItem = new WebItem(instanceName, itemIdCounter++, false).setType(this.type);
        }else{
            webItem = new WebItem(document);
        }

        logger.debug("Before HLA update - WebItem: {}", webItem);

        // Apply updates from HLA
        updateAction.accept(webItem);

        // store back in db
        collection.update(webItem.getDocument(), true);

        // Notify web callbacks of the updated item
        WebItem finalWebItem = webItem;
        this.webItemToWebCallbackList.forEach((callback) -> callback.sendToWeb(finalWebItem));
    }

    public void updateFromWeb(WebItem webItem) {
        try {
            if (webItem.getItemId() == null) {
                // New item creation
                webItem.setItemId(itemIdCounter++);
                fireHlaCallback(webItem);
                collection.insert(webItem.getDocument());
                logger.debug("Created new item: itemId={}, instanceName={}",
                        webItem.getItemId(), webItem.getInstanceName());
            } else {
                // Existing item update
                Document document = collection.find(where(WebItem.ITEM_ID_KEY).eq(webItem.getItemId())).firstOrNull();
                if (document != null) {
                    WebItem currentItem = new WebItem(document);
                    currentItem.replaceWith(webItem.getMap());
                    fireHlaCallback(currentItem);
                    collection.update(currentItem.getDocument());
                    logger.debug("Updated existing item: itemId={}, instanceName={}",
                            currentItem.getItemId(), currentItem.getInstanceName());
                } else {
                    logger.warn("No existing item found for itemId={}, creating a new one.", webItem.getItemId());
                    collection.insert(webItem.getDocument());
                }
            }
        } catch (Exception e) {
            logger.error("Error processing updateFromWeb for itemId={}, reason: {}",
                    webItem.getItemId(), e.getMessage());
            throw new RuntimeException("Error storing item: " + e.getMessage());
        }
    }

    private void fireHlaCallback(WebItem webItem){
        if(webItem.getInstanceName() == null){
            this.webItemToHlaCallback.ifPresentOrElse(
                    (callback)->{
                        String instanceName = callback.sendNewItemToHla(webItem);
                        webItem.setInstanceName(instanceName);
                    },
                    ()->logger.warn("Storing update from web, but there is not HLA callback"));
        }else{
           this.webItemToHlaCallback.ifPresentOrElse((callback)->callback.sendToHla(webItem),
            ()->logger.warn("Storing update from web, but there is not HLA callback"));
        }

    }

    public List<WebItem> getAllItems(){
        return this.collection.find().toList().stream().map((doc)->new WebItem(doc)).toList();
    }

    public List<WebItem> getByField(String fieldName, Object value){
        return this.collection.find(where(fieldName).eq(value)).toList().stream().map((doc)->new WebItem(doc)).toList();
    }

    public Optional<WebItem> getItemById(Integer id){
        Document document = collection.find(where(WebItem.ITEM_ID_KEY).eq(id)).firstOrNull();
        if(document == null){
            return Optional.empty();
        }
        return Optional.of(new WebItem(document));
    }

    public Optional<WebItem> getItemByInstanceName(String instanceName){
        Document document = collection.find(where(WebItem.INSTANCE_NAME_KEY).eq(instanceName)).firstOrNull();
        if(document == null){
            return Optional.empty();
        }
        return Optional.of(new WebItem(document));
    }

    public void requestRelease(String instanceName){
        this.ownershipToHlaCallback.ifPresent((callback)->{
            callback.release(instanceName);
        });
    }

    public void requestAcquire(String instanceName){
        this.ownershipToHlaCallback.ifPresent((callback)->{
            callback.acquire(instanceName);
        });
    }

    public void setWebItemToHlaCallback(WebItemToHlaCallback callback) {
        this.webItemToHlaCallback = Optional.ofNullable(callback);
    }

    public void addWebItemToWebCallback(WebItemToWebCallback callback) {
        this.webItemToWebCallbackList.add(callback);
    }

    public void clearWebItemToHlaCallback() {
        this.webItemToHlaCallback = Optional.empty();
    }

    public void removeWebItemToWebCallback(WebItemToWebCallback callback) {
        this.webItemToWebCallbackList.remove(callback);
    }

    public void setOwnershipToHlaCallback(OwnershipToHlaCallback callback){
        this.ownershipToHlaCallback = Optional.ofNullable(callback);
    }

    public void addTransformer(Transformer transformer) {
        throw new RuntimeException("not implemented yet");
    }

}
